import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String[] testFiles = {
                "/Users/catarosca/IdeaProjects/untitled/src/input_large.txt",
                "/Users/catarosca/IdeaProjects/untitled/src/input_medium.txt",
                "/Users/catarosca/IdeaProjects/untitled/src/input_small.txt"
        };

        for (String filename : testFiles) {
            System.out.println("Rulează pe fișierul: " + filename);
            System.out.println("-----------------------------------");

            // Rezoluție
            //System.out.println("Testare: Rezoluție");
            //ResolutionSolver.main(new String[]{filename});
            //System.out.println("-----------------------------------");

            //Davis-Putnam
            System.out.println("Testare: Davis-Putnam");
            DavisPutnamSolver.main(new String[]{filename});
            System.out.println("-----------------------------------");

            // DPLL
            System.out.println("Testare: DPLL");
            DPLLSolver.main(new String[]{filename});
            System.out.println("-----------------------------------");

            // CDCL
            System.out.println("Testare: CDCL");
            CDCLSolver.main(new String[]{filename});
            System.out.println("-----------------------------------");

            // WalkSAT
            System.out.println("Testare: WalkSAT");
            WalkSATSolver.main(new String[]{filename});
            System.out.println("-----------------------------------");
        }
    }
}
