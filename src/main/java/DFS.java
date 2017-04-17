import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by szale_000 on 2017-04-16.
 */

public class DFS implements PuzzleSolver {

    private List<Action> fetchOrder;
    private List<Action> dsfSolution;
    private double time = 0;
    private int visitedStates = 0;
    private int maxLevel = 0;
    private static final int MAX_ALLOWED_LEVEL = 20;

    @Override
    public void solve(Board board, String params) {
        prepare(params);

        Stack<List<Action>> stack = new Stack<>();
        addActionsToStack(stack, new ArrayList<>(), null);
        List<Action> solution = null;

        while (solution == null || stack.isEmpty()) {
            List<Action> actions = stack.pop();
            visitedStates++;
            //System.out.println("level " + actions.size() + " trying: " + actions);
            try {
                Board tempBoard = new Board(board, actions);
                if (tempBoard.isSolved()) {
                    solution = actions;
                } else if (actions.size() < MAX_ALLOWED_LEVEL) {
                    addActionsToStack(stack, actions, actions.get(actions.size() - 1).opposite());
                }
            } catch (InvalidBoardOperationException e) {
                visitedStates--;
            }
        }
        time = System.nanoTime() - time;
        dsfSolution = solution;
    }

    @Override
    public void printResults(String solutionPath, String statsPath) throws IOException {
        new Utils().printResults(solutionPath, statsPath, dsfSolution, visitedStates, maxLevel, time / Math.pow(10, 6));
    }

    private void addActionsToStack(Stack<List<Action>> stack, List<Action> actionHistory, Action excludedAction) {
        for (Action action : fetchOrder) {
            if (action != excludedAction) {
                List<Action> actionsToFetch = new ArrayList<>(actionHistory);
                actionsToFetch.add(action);
                //System.out.println("adding "+actionsToFetch);
                stack.push(actionsToFetch);
            }
        }
        if (actionHistory.size() + 1 > maxLevel) {
            maxLevel = actionHistory.size() + 1;
        }
    }

    private void prepare(String fetchOrder) {
        this.fetchOrder = new Utils().parseBruteForceParameters(fetchOrder);
        Collections.reverse(this.fetchOrder);
        visitedStates = 0;
        time = System.nanoTime();
    }

}