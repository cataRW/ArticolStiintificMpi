import java.io.*;
import java.util.*;

public class WalkSATSolver {

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

    public static boolean walkSAT(List<List<String>> formula, int maxFlips) {
        Map<String, Boolean> assignment = new HashMap<>();
        Random random = new Random();

        // Inițializare aleatorie
        for (List<String> clause : formula) {
            for (String lit : clause) {
                assignment.putIfAbsent(stripNegation(lit), random.nextBoolean());
            }
        }

        for (int flip = 0; flip < maxFlips; flip++) {
            if (isSatisfied(formula, assignment)) return true;

            // Găsește o clauză falsă
            List<String> falseClause = getFalseClause(formula, assignment, random);
            if (falseClause == null) return true;

            // Flipping aleator al unei variabile din clauza falsă
            String litToFlip = falseClause.get(random.nextInt(falseClause.size()));
            String var = stripNegation(litToFlip);
            assignment.put(var, !assignment.get(var));
        }

        return false;
    }

    public static List<String> getFalseClause(List<List<String>> formula, Map<String, Boolean> assignment, Random random) {
        List<List<String>> falseClauses = new ArrayList<>();
        for (List<String> clause : formula) {
            boolean satisfied = false;
            for (String lit : clause) {
                Boolean val = assignment.get(stripNegation(lit));
                if (val != null && ((lit.startsWith("¬") && !val) || (!lit.startsWith("¬") && val))) {
                    satisfied = true;
                    break;
                }
            }
            if (!satisfied) {
                falseClauses.add(clause);
            }
        }
        return falseClauses.isEmpty() ? null : falseClauses.get(random.nextInt(falseClauses.size()));
    }

    public static boolean isSatisfied(List<List<String>> formula, Map<String, Boolean> assignment) {
        for (List<String> clause : formula) {
            boolean satisfied = false;
            for (String lit : clause) {
                Boolean val = assignment.get(stripNegation(lit));
                if (val != null && ((lit.startsWith("¬") && !val) || (!lit.startsWith("¬") && val))) {
                    satisfied = true;
                    break;
                }
            }
            if (!satisfied) return false;
        }
        return true;
    }

    public static String stripNegation(String literal) {
        return literal.startsWith("¬") ? literal.substring(1) : literal;
    }

    public static void main(String[] args) throws IOException {
        String filename = args[0];
        System.out.println("Rezolvare cu WalkSAT pentru: " + filename);
        List<List<String>> formula = parseFormulaFromFile(filename);
        boolean satisfiable = walkSAT(formula, 10000); // 10.000 încercări maxime
        System.out.println(satisfiable ? "SATISFIABIL" : "NESATISFIABIL");
    }
}
