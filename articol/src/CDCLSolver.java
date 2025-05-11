import java.io.*;
import java.util.*;

public class CDCLSolver {

    public static List<List<String>> parseFormulaFromFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine();
        br.close();

        List<List<String>> formula = new ArrayList<>();
        String[] clauses = line.split("∧");
        for (String clause : clauses) {
            clause = clause.replaceAll("[()\\s]", "");
            String[] literals = clause.split("∨");
            formula.add(new ArrayList<>(Arrays.asList(literals)));
        }
        return formula;
    }

    public static boolean cdcl(List<List<String>> formula) {
        Stack<String> trail = new Stack<>();
        Map<String, Boolean> assignments = new HashMap<>();

        while (true) {
            boolean conflict = unitPropagation(formula, assignments, trail);
            if (conflict) {
                if (trail.isEmpty()) {
                    return false; // conflict global
                }
                // Backtrack simplificat
                String last = trail.pop();
                assignments.remove(last);
            } else {
                if (isSatisfied(formula, assignments)) {
                    return true;
                }
                String decision = chooseLiteral(formula, assignments);
                if (decision == null) {
                    return true;
                }
                trail.push(decision);
                assignments.put(decision, true);
            }
        }
    }

    public static boolean unitPropagation(List<List<String>> formula, Map<String, Boolean> assignments, Stack<String> trail) {
        boolean progress;
        do {
            progress = false;
            for (List<String> clause : formula) {
                List<String> unassigned = new ArrayList<>();
                boolean satisfied = false;
                for (String lit : clause) {
                    Boolean val = assignments.get(stripNegation(lit));
                    if (val == null) {
                        unassigned.add(lit);
                    } else if ((lit.startsWith("¬") && !val) || (!lit.startsWith("¬") && val)) {
                        satisfied = true;
                        break;
                    }
                }
                if (!satisfied && unassigned.size() == 1) {
                    String unit = unassigned.get(0);
                    trail.push(unit);
                    assignments.put(stripNegation(unit), !unit.startsWith("¬"));
                    progress = true;
                } else if (!satisfied && unassigned.isEmpty()) {
                    return true; // conflict
                }
            }
        } while (progress);
        return false;
    }

    public static boolean isSatisfied(List<List<String>> formula, Map<String, Boolean> assignments) {
        for (List<String> clause : formula) {
            boolean satisfied = false;
            for (String lit : clause) {
                Boolean val = assignments.get(stripNegation(lit));
                if (val != null && ((lit.startsWith("¬") && !val) || (!lit.startsWith("¬") && val))) {
                    satisfied = true;
                    break;
                }
            }
            if (!satisfied) return false;
        }
        return true;
    }

    public static String chooseLiteral(List<List<String>> formula, Map<String, Boolean> assignments) {
        for (List<String> clause : formula) {
            for (String lit : clause) {
                if (!assignments.containsKey(stripNegation(lit))) {
                    return stripNegation(lit);
                }
            }
        }
        return null;
    }

    public static String stripNegation(String literal) {
        return literal.startsWith("¬") ? literal.substring(1) : literal;
    }

    public static void main(String[] args) throws IOException {
        String filename = args[0];
        System.out.println("Rezolvare cu CDCL pentru: " + filename);
        List<List<String>> formula = parseFormulaFromFile(filename);
        boolean satisfiable = cdcl(formula);
        System.out.println(satisfiable ? "SATISFIABIL" : "NESATISFIABIL");
    }
}
