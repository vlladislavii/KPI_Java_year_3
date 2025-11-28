package org.example.lab.lab2;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

/**
 * Custom Gatherer that skips the first N elements matching a specific creature type
 * and then collects exactly a specified number of elements.
 * <p>
 * This Gatherer implements the requirement from Task 4: skip first N objects
 * by a certain value from Field A (creature type) and collect exactly 500 elements.
 * <p>
 */
public class SkipByFieldGatherer implements Gatherer<ChthoniceCreature, SkipByFieldGatherer.State, ChthoniceCreature> {

    private final CreatureType typeToSkip;
    private final int numberOfElementsToSkip;
    private final int targetSize;

    /**
     * State object that tracks the progress of the gathering operation.
     */
    static class State {
        int skippedCount = 0;
        int collectedCount = 0;
    }

    /**
     * Creates a new SkipByFieldGatherer.
     *
     * @param typeToSkip            the creature type to skip
     * @param numberOfElementsToSkip the number of elements of that type to skip
     * @param targetSize            the target number of elements to collect
     */
    public SkipByFieldGatherer(CreatureType typeToSkip, int numberOfElementsToSkip, int targetSize) {
        this.typeToSkip = typeToSkip;
        this.numberOfElementsToSkip = numberOfElementsToSkip;
        this.targetSize = targetSize;
    }

    @Override
    public Supplier<State> initializer() {
        return State::new;
    }

    @Override
    public Integrator<State, ChthoniceCreature, ChthoniceCreature> integrator() {
        return (state, element, downstream) -> {
            // If we've already collected enough elements, stop processing
            if (state.collectedCount >= targetSize) {
                return false; // Stop processing
            }

            // Check if this element should be skipped
            if (element.getType() == typeToSkip && state.skippedCount < numberOfElementsToSkip) {
                state.skippedCount++;
                return true; // Continue processing, but don't emit this element
            }

            // Collect this element
            state.collectedCount++;
            downstream.push(element);

            // Continue processing if we haven't reached the target size
            return state.collectedCount < targetSize;
        };
    }

    @Override
    public BinaryOperator<State> combiner() {
        return (state1, state2) -> {
            // Combine two states (for parallel streams)
            State combined = new State();
            combined.skippedCount = state1.skippedCount + state2.skippedCount;
            combined.collectedCount = state1.collectedCount + state2.collectedCount;
            return combined;
        };
    }

    @Override
    public BiConsumer<State, Downstream<? super ChthoniceCreature>> finisher() {
        return (state, downstream) -> {
            // No additional finalization needed
        };
    }

    /**
     * Creates a gatherer that skips the first N elements of a specific type
     * and collects exactly 500 elements.
     *
     * @param typeToSkip the creature type to skip
     * @param n          the number of elements to skip
     * @return a new SkipByFieldGatherer
     */
    public static SkipByFieldGatherer  skipAndCollect(CreatureType typeToSkip, int n) {
        return new SkipByFieldGatherer(typeToSkip, n, 500);
    }

    /**
     * Creates a gatherer that skips the first N elements of a specific type
     * and collects exactly the specified number of elements.
     *
     * @param typeToSkip the creature type to skip
     * @param n          the number of elements to skip
     * @param targetSize the target size to collect
     * @return a new SkipByFieldGatherer
     */
    public static SkipByFieldGatherer skipAndCollect(CreatureType typeToSkip, int n, int targetSize) {
        return new SkipByFieldGatherer(typeToSkip, n, targetSize);
    }
}