package ch.kup.nono.solver;

import ch.kup.nono.CellState;
import ch.kup.nono.Hint;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ch.kup.nono.CellState.*;
import static java.lang.Math.max;
import static java.lang.Math.min;

public abstract class NonoLineSolver implements NonoLine {
    public void performSolveStep() {
        if (!done()) performSlack();
        if (!done()) checkForAnchors();
        if (!done()) handleSubLines();
        if (!done()) workOnIdentifiedHints();
        if (!done()) finishOffIfCompleted();
    }

    private boolean done() {
        for (int i = 0; i < size(); i++) {
            if (getState(i) == UNTOUCHED) return false;
        }
        return true;
    }

    public boolean isOk() {
        if (!done()) return false;
        List<Range> ranges = breakOnEmptyCells();
        List<Hint> hintsFromSolution = ranges.stream().map(r -> new Hint(r.length())).collect(Collectors.toList());
        if (hintsFromSolution.size() != getHints().size()) return false;
        for (int i = 0; i < hintsFromSolution.size(); i++) {
            if (hintsFromSolution.get(i).getLength() != getHints().get(i).getLength()) return false;
        }
        return true;
    }

    private void performSlack() {
        if (getHints().size() == 0) {
            IntStream.range(0, size()).forEach(i -> setState(i, EMPTY));
            return;
        }

        int slack = size() - (getHints().stream().mapToInt(Hint::getLength).sum() + getHints().size() - 1);
        int pos = 0;
        for (Hint hint : getHints()) {
            if (hint.getLength() > slack) {
                for (int i = pos + slack; i < pos + hint.getLength(); i++) {
                    setState(i, CellState.FILLED);
                }
            }
            if (slack == 0 && pos + hint.getLength() < size()) {
                setState(pos + hint.getLength(), EMPTY);
            }
            pos += hint.getLength() + 1;
        }
    }

    private void finishOffIfCompleted() {
        if (getFilledCount() == getTotalHintLength()) {
            IntStream.range(0, size()).forEach(i -> {
                if (getState(i) == UNTOUCHED) setState(i, EMPTY);
            });
        }
    }

    private void checkForAnchors() {
        if (getState(0) == FILLED) {
            handleBoundary(0, -1);
        }
        for (int i = 0; i < size() - 1; i++) {
            if (getState(i) == FILLED && getState(i + 1) == EMPTY) {
                handleBoundary(i, i + 1);
            }
            if (getState(i) == EMPTY && getState(i + 1) == FILLED) {
                handleBoundary(i + 1, i);
            }
        }
        if (getState(size() - 1) == FILLED) {
            handleBoundary(size() - 1, size());
        }
    }

    private void handleSubLines() {
        if (getHints().size() == 0) {
            return;
        }

        List<Range> ranges = breakOnEmptyCells();
        if (ranges.size() == 1 && ranges.get(0).length() == size()) return;

        List<Map<Range, List<Hint>>> mapHintsToRanges = mapHintsToRanges(ranges);
        if (mapHintsToRanges.size() == 0) {
            throw new IllegalStateException("Nothing fits");
        }
        for (int i = 0; i < ranges.size(); i++) {
            // start with a single range (ranges.get(i)) and then expand to the right
            // by taking more and more ranges
            for (int rangeCount = 1; rangeCount <= ranges.size() - i; rangeCount++) {
                List<Range> rangeList = ranges.subList(i, i + rangeCount);
                List<Hint> hintsForRangeList = getUniqueListOfHintsForRanges(mapHintsToRanges, rangeList);
                if (hintsForRangeList != null) {
                    NonoLineSolver subSolver = createNonoLineSolver(this, rangeList, hintsForRangeList);
                    if (subSolver.size() < size()) {
                        subSolver.performSolveStep();
                    }
                    break;
                }
            }
        }
    }

    private List<Hint> getUniqueListOfHintsForRanges(List<Map<Range, List<Hint>>> mapHintsToRanges, List<Range> ranges) {
        List<List<Hint>> possibleHintListsForTheRanges = mapHintsToRanges.stream()
                .map(mapping -> mapping.entrySet()
                        .stream()
                        .filter(e -> ranges.contains(e.getKey()))
                        .sorted(Comparator.comparing(e -> e.getKey().start))
                        .map(Map.Entry::getValue)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList())
                )
                .distinct()
                .collect(Collectors.toList());
        if (possibleHintListsForTheRanges.size() == 1) {
            return possibleHintListsForTheRanges.get(0);
        }
        return null;
    }

    private NonoLineSolver createNonoLineSolver(NonoLineSolver parent, List<Range> ranges, List<Hint> hints) {
        final int start = ranges.get(0).start;
        final int end = ranges.get(ranges.size() - 1).end;
        return new NonoLineSolver() {

            @Override
            public int size() {
                return end - start + 1;
            }

            @Override
            public CellState getState(int i) {
                return parent.getState(start + i);
            }

            @Override
            public void setState(int i, CellState state) {
                parent.setState(start + i, state);
            }

            @Override
            public List<Hint> getHints() {
                return hints;
            }

            @Override
            public String toString() {
                return "Sub - " + super.toString();
            }
        };
    }

    private List<Range> breakOnEmptyCells() {
        List<Range> res = new ArrayList<>();
        Range currentRange = null;
        for (int i = 0; i < size(); i++) {
            if (getState(i) == EMPTY) {
                if (currentRange != null) {
                    currentRange.end = i - 1;
                    res.add(currentRange);
                    currentRange = null;
                }
            } else {
                if (currentRange == null) {
                    currentRange = new Range();
                    currentRange.start = i;
                }
            }
        }
        if (currentRange != null) {
            currentRange.end = size() - 1;
            res.add(currentRange);
        }

        return res;
    }

    /**
     * enumerate all possible assignments of hints to ranges. For the first range
     * we take 0, 1, or up to all hints. In the second we continue with the rest - also
     * counting from 0 to the number of hints.
     */
    private List<Map<Range, List<Hint>>> mapHintsToRanges(List<Range> ranges) {
        return mapHintsToRanges(getHints(), ranges);
    }


    private List<Map<Range, List<Hint>>> mapHintsToRanges(List<Hint> hints, List<Range> ranges) {
        if (ranges.size() == 0) {
            if (hints.size() > 0) {
                return Collections.emptyList();
            } else {
                return Collections.singletonList(Collections.emptyMap());
            }
        }

        List<Map<Range, List<Hint>>> res = new ArrayList<>();
        for (int i = 0; i <= hints.size(); i++) {
            List<Hint> hintsForTheFirstRange = hints.subList(0, i);
            if (rangeCouldFitHints(ranges.get(0), hintsForTheFirstRange)) {
                List<Map<Range, List<Hint>>> recurseResult = mapHintsToRanges(hints.subList(i, hints.size()), ranges.subList(1, ranges.size()));
                for (Map<Range, List<Hint>> recurseResultItem : recurseResult) {
                    Map<Range, List<Hint>> extendedItem = new HashMap<>();
                    extendedItem.put(ranges.get(0), hintsForTheFirstRange);
                    extendedItem.putAll(recurseResultItem);
                    res.add(extendedItem);
                }
            }
        }

        return res;
    }

    private boolean rangeCouldFitHints(Range range, List<Hint> hints) {
        int filledCount = 0;
        for (int i = range.start; i <= range.end; i++) {
            if (getState(i) == FILLED) {
                filledCount++;
            }
        }

        int filledCountOfHints = 0;
        for (Hint hint : hints) {
            filledCountOfHints += hint.getLength();
        }

        if (filledCount > filledCountOfHints)
            return false;


        int sizeOfHints = filledCountOfHints + hints.size() - 1;

        return range.length() >= sizeOfHints;
    }


    private void workOnIdentifiedHints() {
        for (int i = 0; i < size(); i++) {
            if (getState(i) == FILLED) {
                List<Hint> possibleHints = getPossibleHints(i);
                if (possibleHints.size() == 1) {
                    workOnIdentifiedHint(i, possibleHints.get(0));
                }
            }
        }
    }

    private void workOnIdentifiedHint(int pos, Hint hint) {
        Range range = getCurrentRangeOf(pos);
        if (range.length() == hint.getLength()) {
            if (range.start > 0 && getState(range.start - 1) == UNTOUCHED) {
                setState(range.start - 1, EMPTY);
            }
            if (range.end < size() - 1 && getState(range.end + 1) == UNTOUCHED) {
                setState(range.end + 1, EMPTY);
            }
        }

        // first hint -- fill the left side
        if (getHints().indexOf(hint) == 0) {
            int leftMostPossibleFilledPosition = range.start - (hint.getLength() - range.length());
            for (int i = 0; i < leftMostPossibleFilledPosition; i++) {
                setState(i, EMPTY);
            }
        }

        // last hint -- fill the right side
        if (getHints().indexOf(hint) == getHints().size() - 1) {
            int rightmostPossibleFilledPosition = range.end + (hint.getLength() - range.length());
            for (int i = rightmostPossibleFilledPosition + 1; i < size(); i++) {
                setState(i, EMPTY);
            }
        }

        int expandToTheRight = findMaxExpansionToTheRight(range, hint);
        int expandToTheLeft = findMaxExpansionToTheLeft(range, hint);
        int missingCells = hint.getLength() - range.length();
        // if we cannot move far enough to the right we can mark the difference on the left
        if (expandToTheRight < missingCells) {
            for (int i = 0; i < missingCells - expandToTheRight; i++) {
                setState(range.start - 1 - i, FILLED);
            }
        }
        // and vice versa
        if (expandToTheLeft < missingCells) {
            for (int i = 0; i < missingCells - expandToTheLeft; i++) {
                setState(range.end + 1 + i, FILLED);
            }
        }
    }

    private int findMaxExpansionToTheRight(Range range, Hint hint) {
        int expansion = 0;
        for (int i = range.end + 1; i < size(); i++) {
            if (isPosPossibleExpansionOfRangeAssumingPosBeforeAreOk(i, range, hint)) {
                expansion++;
            } else {
                break;
            }
        }
        return expansion;
    }

    private int findMaxExpansionToTheLeft(Range range, Hint hint) {
        int expansion = 0;
        for (int i = range.start - 1; i >= 0; i--) {
            if (isPosPossibleExpansionOfRangeAssumingPosBeforeAreOk(i, range, hint)) {
                expansion++;
            } else {
                break;
            }
        }
        return expansion;
    }

    private boolean isPosPossibleExpansionOfRangeAssumingPosBeforeAreOk(int pos, Range range, Hint hint) {
        int len = pos < range.start ? range.end - pos + 1 : pos - range.start + 1;
        if (len > hint.getLength())
            return false;
        if (getState(pos) == EMPTY)
            return false;

        if (getState(pos) == FILLED) {
            // check the joined ranges
            Range otherRange = getCurrentRangeOf(pos);
            int start = min(range.start, otherRange.start);
            int end = max(range.end, otherRange.end);
            int combinedLenth = end - start + 1;
            if (combinedLenth > hint.getLength())
                return false;
        }

        return true;
    }

    /**
     * i is a filled position next to a boundary
     */
    private void handleBoundary(int filledPos, int emptyPos) {
        Set<Hint> hints = new HashSet<>(getPossibleHints(filledPos));
        if (hints.size() == 1) {
            completeBoundaryWithSize(filledPos, emptyPos, hints.iterator().next().getLength());
        } else {
            int min = hints.stream().mapToInt(Hint::getLength).min().getAsInt();
            makeBoundedBiggerToAtLeast(filledPos, emptyPos, min);
        }
    }

    private void makeBoundedBiggerToAtLeast(int filledPos, int emptyPos, int newSize) {
        int step = filledPos - emptyPos;
        for (int i = 0; i < newSize; i++) {
            if (getState(filledPos + i * step) != FILLED)
                setState(filledPos + i * step, FILLED);
        }
    }

    private void completeBoundaryWithSize(int filledPos, int emptyPos, int newSize) {
        makeBoundedBiggerToAtLeast(filledPos, emptyPos, newSize);
        int otherBoundary = (filledPos - emptyPos) * newSize + filledPos;
        if (otherBoundary >= 0 && otherBoundary < size()) {
            if (getState(otherBoundary) != EMPTY)
                setState(otherBoundary, EMPTY);
        }
    }

    private List<Hint> getPossibleHints(int pos) {
        Range range = getCurrentRangeOf(pos);
        return getHints().stream()
                .filter(h -> hintCanPossiblyMatchRange(h, range))
                .collect(Collectors.toList());
    }

    private boolean hintCanPossiblyMatchRange(Hint hint, Range range) {
        if (range.start == 0) {
            return getHints().indexOf(hint) == 0;
        }
        if (range.end == size() - 1) {
            return getHints().indexOf(hint) == getHints().size() - 1;
        }

        if (hint.getLength() < range.length())
            return false;

        if (range.start < getLeftmostPossibleStart(hint))
            return false;

        if (range.end > getRightmostPossibleStart(hint))
            return false;

        return true;
    }

    private int getLeftmostPossibleStart(Hint hint) {
        int sum = 0;
        for (int i = 0; i < getHints().indexOf(hint); i++) {
            sum += getHints().get(i).getLength();
            sum++;
        }
        return sum;
    }

    private int getRightmostPossibleStart(Hint hint) {
        int sum = 0;
        for (int i = getHints().size() - 1; i > getHints().indexOf(hint); i--) {
            sum += getHints().get(i).getLength();
            sum++;
        }
        return size() - 1 - sum;
    }

    private static class Range {
        int start; // inclusive
        int end; // inclusive

        int length() {
            return end - start + 1;
        }
    }

    private Range getCurrentRangeOf(int i) {
        Range range = new Range();
        range.start = i;
        range.end = i;
        for (int up = 1; i + up < size(); up++) {
            if (getState(i + up) != FILLED) {
                range.end = i + up - 1;
                break;
            }
        }
        for (int down = 1; i - down >= 0; down++) {
            if (getState(i - down) != FILLED) {
                range.start = i - down + 1;
                break;
            }
        }
        return range;
    }

    private int getFilledCount() {
        return (int) IntStream.range(0, size())
                .mapToObj(this::getState)
                .filter(s -> s == FILLED)
                .count();
    }

    private int getTotalHintLength() {
        return getHints().stream().mapToInt(Hint::getLength).sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            sb.append(getState(i).digit());
        }
        return getHints().toString() + " " + sb.toString();
    }
}
