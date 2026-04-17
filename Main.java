import java.util.*;
import java.util.regex.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   WELCOME TO TRUTH TABLE GENERATOR");
        System.out.println("=======================================");

        while (true) {
            showMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    generateTruthTable();
                    break;
                case "2":
                    manualEvaluation();
                    break;
                case "3":
                    logicGame();
                    break;
                case "4":
                    System.out.println("\nThank you for using the system!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void showMenu() {
        System.out.println("\n=========== MAIN MENU ===========");
        System.out.println("1. Generate Full Truth Table");
        System.out.println("2. Manual Evaluation (Assign T/F)");
        System.out.println("3. Logic Quiz Game (3 Lives, 5 Questions)");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    static void generateTruthTable() {
        String expr = buildExpression();
        runExpression(expr);
    }

    static void manualEvaluation() {
        String expr = buildExpression();
        Set<Character> variables = extractVariables(expr);
        Map<Character, Boolean> values = new HashMap<>();

        System.out.println("\n--- Assign Truth Values ---");
        for (char v : variables) {
            while (true) {
                System.out.print("Enter value for " + v + " (T/F): ");
                String input = scanner.nextLine().toUpperCase();
                if (input.equals("T") || input.equals("F")) {
                    values.put(v, input.equals("T"));
                    break;
                }
                System.out.println("Invalid input.");
            }
        }

        boolean result = evaluate(expr, values);
        System.out.println("\nResult: " + expr + " = " + (result ? "TRUE (T)" : "FALSE (F)"));
    }

    static String buildExpression() {

        System.out.println("\n--- Build Logical Expression ---");

        System.out.print("Enter first variable (P, Q, R): ");
        String v1 = scanner.nextLine().toUpperCase();

        System.out.println("Choose operator:");
        System.out.println("1. NOT");
        System.out.println("2. AND");
        System.out.println("3. OR");
        System.out.println("4. XOR");
        System.out.println("5. IMPLIES (->)");
        System.out.println("6. BICONDITIONAL (<->)");

        String opChoice = scanner.nextLine();

        if (opChoice.equals("1")) {
            String expr = "NOT " + v1;
            System.out.println("Expression: " + expr);
            return expr;
        }

        System.out.print("Enter second variable (P, Q, R): ");
        String v2 = scanner.nextLine().toUpperCase();

        String operator = switch (opChoice) {
            case "2" -> "AND";
            case "3" -> "OR";
            case "4" -> "XOR";
            case "5" -> "->";
            case "6" -> "<->";
            default -> "AND";
        };

        String expr = v1 + " " + operator + " " + v2;
        System.out.println("Expression: " + expr);

        return expr;
    }

    static void runExpression(String expr) {
        Set<Character> variables = extractVariables(expr);
        List<Character> varList = new ArrayList<>(variables);
        Collections.sort(varList);

        int rows = (int) Math.pow(2, varList.size());

        System.out.println("\nTruth Table for: " + expr);

        for (char v : varList) System.out.print(v + "\t");
        System.out.println("| Result");
        System.out.println("--------------------------------");

        for (int i = 0; i < rows; i++) {
            Map<Character, Boolean> values = new HashMap<>();

            for (int j = 0; j < varList.size(); j++) {
                boolean val = (i & (1 << (varList.size() - j - 1))) != 0;
                values.put(varList.get(j), val);
                System.out.print((val ? "T" : "F") + "\t");
            }

            boolean result = evaluate(expr, values);
            System.out.println("| " + (result ? "T" : "F"));
        }
    }

    static void logicGame() {

        System.out.println("\n================================");
        System.out.println("      LOGIC QUIZ GAME 🎮");
        System.out.println("   3 Lives | Max 5 Questions");
        System.out.println("================================\n");

        Random rand = new Random();
        int lives = 3;
        int questionsAsked = 0;

        String[] questions = {
                "P AND Q",
                "P OR Q",
                "P XOR Q",
                "NOT P",
                "P -> Q",
                "P <-> Q"
        };

        while (lives > 0 && questionsAsked < 5) {

            String expr = questions[rand.nextInt(questions.length)];

            Map<Character, Boolean> values = new HashMap<>();
            values.put('P', rand.nextBoolean());
            values.put('Q', rand.nextBoolean());

            boolean correctAnswer = evaluate(expr, values);

            System.out.println("\n--------------------------------");
            System.out.println("Question " + (questionsAsked + 1));
            System.out.println("Expression: " + expr);
            System.out.println("P = " + (values.get('P') ? "T" : "F"));
            System.out.println("Q = " + (values.get('Q') ? "T" : "F"));

            System.out.print("Your answer (T/F): ");
            String answer = scanner.nextLine().toUpperCase();

            boolean userAnswer = answer.equals("T");

            if (userAnswer == correctAnswer) {
                System.out.println("Correct!");
            } else {
                lives--;
                System.out.println("Wrong!");
                System.out.println("Lives remaining: " + lives);
            }

            questionsAsked++;

            if (lives == 0 || questionsAsked == 5) {
                break;
            }

            System.out.print("\nDo you want to continue? (Y/N): ");
            String cont = scanner.nextLine().toUpperCase();

            if (!cont.equals("Y")) {
                System.out.println("\nExiting game...");
                break;
            }
        }

        System.out.println("\n================================");
        System.out.println("GAME OVER");
        System.out.println("Questions Answered: " + questionsAsked);
        System.out.println("================================");
    }

    static Set<Character> extractVariables(String expr) {
        Set<Character> vars = new HashSet<>();
        for (char c : expr.toCharArray()) {
            if (c >= 'A' && c <= 'Z' && c != 'N' && c != 'O' && c != 'T'
                    && c != 'A' && c != 'D' && c != 'R' && c != 'X') {
                vars.add(c);
            }
        }
        return vars;
    }

    static boolean evaluate(String expr, Map<Character, Boolean> values) {
        for (Map.Entry<Character, Boolean> e : values.entrySet()) {
            expr = expr.replace(e.getKey().toString(), e.getValue() ? "T" : "F");
        }
        expr = expr.replaceAll("\\s+", "");
        return eval(expr);
    }

    static boolean eval(String expr) {

        while (expr.contains("(")) {
            int close = expr.indexOf(")");
            int open = expr.lastIndexOf("(", close);
            boolean val = eval(expr.substring(open + 1, close));
            expr = expr.substring(0, open) + (val ? "T" : "F") + expr.substring(close + 1);
        }

        expr = expr.replace("NOTT", "F").replace("NOTF", "T");

        String[] ops = {"AND", "OR", "XOR", "->", "<->"};
        for (String op : ops) {
            expr = process(expr, op);
        }

        return expr.equals("T");
    }

    static String process(String expr, String op) {

        String regex = switch (op) {
            case "AND" -> "([TF])AND([TF])";
            case "OR" -> "([TF])OR([TF])";
            case "XOR" -> "([TF])XOR([TF])";
            case "->" -> "([TF])->([TF])";
            case "<->" -> "([TF])<->([TF])";
            default -> "";
        };

        Pattern pattern = Pattern.compile(regex);

        while (true) {
            Matcher m = pattern.matcher(expr);
            if (!m.find()) break;

            boolean A = m.group(1).equals("T");
            boolean B = m.group(2).equals("T");

            boolean res = switch (op) {
                case "AND" -> A && B;
                case "OR" -> A || B;
                case "XOR" -> A ^ B;
                case "->" -> !A || B;
                case "<->" -> A == B;
                default -> false;
            };

            expr = expr.substring(0, m.start()) +
                    (res ? "T" : "F") +
                    expr.substring(m.end());
        }

        return expr;
    }
}