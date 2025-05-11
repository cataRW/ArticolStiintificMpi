import java.io.*;
import java.util.*;

public class DPLLSolver {

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

    public static boolean dpll(List<List<String>> formula, Set<String> assignment) {
        if (formula.isEmpty()) return true;
        for (List<String> clause : formula) {
            if (clause.isEmpty()) return false;
        }

        String literal = findUnitClause(formula);
        if (literal != null) {
            assignment.add(literal);
            return dpll(simplify(formula, literal), assignment);
        }

        literal = chooseLiteral(formula);
        Set<String> assignmentCopy = new HashSet<>(assignment);
        assignmentCopy.add(literal);
        if (dpll(simplify(formula, literal), assignmentCopy)) return true;

        assignment.add(negate(literal));
        return dpll(simplify(formula, negate(literal)), assignment);
    }

    public static String findUnitClause(List<List<String>> formula) {
        for (List<String> clause : formula) {
            if (clause.size() == 1) return clause.get(0);
        }
        return null;
    }

    public static String chooseLiteral(List<List<String>> formula) {
        for (List<String> clause : formula) {
            if (!clause.isEmpty()) return clause.get(0);
        }
        return null;
    }

    public static String negate(String literal) {
        return literal.startsWith("¬") ? literal.substring(1) : "¬" + literal;
    }

    public static List<List<String>> simplify(List<List<String>> formula, String literal) {
        List<List<String>> result = new ArrayList<>();
        for (List<String> clause : formula) {
            if (clause.contains(literal)) continue;
            List<String> newClause = new ArrayList<>(clause);
            newClause.remove(negate(literal));
            result.add(newClause);
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        String filename = args[0];
        System.out.println("Rezolvare cu DPLL pentru: " + filename);
        List<List<String>> formula = parseFormulaFromFile(filename);
        boolean satisfiable = dpll(formula, new HashSet<>());
        System.out.println(satisfiable ? "SATISFIABIL" : "NESATISFIABIL");
    }
}
