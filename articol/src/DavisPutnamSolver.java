import java.util.*;
import java.io.*;

public class DavisPutnamSolver {

    // Citește formula dintr-un fișier text
    public static Set<Set<String>> parseFormulaFromFile(String filename) throws IOException {
        Set<Set<String>> formula = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                formula.add(new HashSet<>(Arrays.asList(line.trim().split("\\s+"))));
            }
        }
        return formula;
    }

    // Aplică algoritmul Davis-Putnam recursiv
    public static boolean davisPutnam(Set<Set<String>> formula) {
        if (formula.isEmpty()) return true;
        if (formula.contains(new HashSet<>())) return false;

        String pureLiteral = findPureLiteral(formula);
        if (pureLiteral != null) {
            return davisPutnam(simplifyFormula(formula, pureLiteral));
        }

        String unitLiteral = findUnitClause(formula);
        if (unitLiteral != null) {
            return davisPutnam(simplifyFormula(formula, unitLiteral));
        }

        String literal = formula.iterator().next().iterator().next();
        return davisPutnam(simplifyFormula(formula, literal)) || davisPutnam(simplifyFormula(formula, negate(literal)));
    }

    // Găsește un literal pur (dacă există)
    private static String findPureLiteral(Set<Set<String>> formula) {
        Map<String, Integer> counts = new HashMap<>();
        for (Set<String> clause : formula) {
            for (String literal : clause) {
                counts.put(literal, counts.getOrDefault(literal, 0) + 1);
            }
        }
        for (String literal : counts.keySet()) {
            if (!counts.containsKey(negate(literal))) {
                return literal;
            }
        }
        return null;
    }

    // Găsește o clauză unitară (dacă există)
    private static String findUnitClause(Set<Set<String>> formula) {
        for (Set<String> clause : formula) {
            if (clause.size() == 1) {
                return clause.iterator().next();
            }
        }
        return null;
    }

    // Simplifică formula pe baza unui literal
    private static Set<Set<String>> simplifyFormula(Set<Set<String>> formula, String literal) {
        Set<Set<String>> newFormula = new HashSet<>();
        for (Set<String> clause : formula) {
            if (clause.contains(literal)) continue;
            Set<String> newClause = new HashSet<>(clause);
            newClause.remove(negate(literal));
            newFormula.add(newClause);
        }
        return newFormula;
    }

    // Obține negația unui literal
    private static String negate(String literal) {
        return literal.startsWith("~") ? literal.substring(1) : "~" + literal;
    }

    // Punct de pornire - se așteaptă un fișier ca argument
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java DavisPutnamSolver <input_file>");
            return;
        }
        Set<Set<String>> formula = parseFormulaFromFile(args[0]);
        long start = System.currentTimeMillis();
        boolean result = davisPutnam(formula);
        long end = System.currentTimeMillis();
        System.out.println("Satisfiabil: " + result);
        System.out.println("Timp de execuție: " + (end - start) + " ms");
    }
}
