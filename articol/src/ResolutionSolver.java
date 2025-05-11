import java.io.*;
import java.util.*;

public class ResolutionSolver {

    public static List<List<String>> parseFormulaFromFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine();
        br.close();

        List<List<String>> formula = new ArrayList<>();
        String[] clauses = line.split("∧");
        for (String clause : clauses) {
            clause = clause.replaceAll("[()\\s]", "");
            String[] literals = clause.split("∨");
            formula.add(Arrays.asList(literals));
        }
        return formula;
    }

    public static boolean applyResolution(List<List<String>> formula) {
        Set<String> derived = new HashSet<>();
        Queue<List<String>> queue = new LinkedList<>(formula);
        int steps = 0;
        int maxSteps = 1000;

        while (!queue.isEmpty() && steps < maxSteps) {
            List<String> clause1 = queue.poll();
            List<List<String>> queueSnapshot = new ArrayList<>(queue);
            boolean newClauseGenerated = false;

            for (List<String> clause2 : queueSnapshot) {
                for (String lit1 : clause1) {
                    for (String lit2 : clause2) {
                        if (lit1.equals("¬" + lit2) || lit2.equals("¬" + lit1)) {
                            Set<String> newClause = new HashSet<>(clause1);
                            newClause.addAll(clause2);
                            newClause.remove(lit1);
                            newClause.remove(lit2);
                            if (newClause.isEmpty()) return false; // Conflict
                            String clauseStr = String.join(",", newClause);
                            if (!derived.contains(clauseStr)) {
                                derived.add(clauseStr);
                                queue.add(new ArrayList<>(newClause));
                                newClauseGenerated = true;
                            }
                        }
                    }
                }
            }

            if (!newClauseGenerated) break; // Nimic nou generat, stop
            steps++;
        }

        return true; // Nu s-a ajuns la conflict în limitele stabilite
    }


    public static void main(String[] args) throws IOException {
        String filename = args[0];
        System.out.println("Rezolvare cu Rezoluție pentru: " + filename);
        List<List<String>> formula = parseFormulaFromFile(filename);
        boolean satisfiable = applyResolution(formula);
        System.out.println(satisfiable ? "SATISFIABIL" : "NESATISFIABIL");
    }
}
