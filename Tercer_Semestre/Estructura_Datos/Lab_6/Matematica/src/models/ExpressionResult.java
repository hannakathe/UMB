package models;

public class ExpressionResult {
    private String postfix;
    private double result;

    public ExpressionResult(String postfix, double result) {
        this.postfix = postfix;
        this.result = result;
    }

    public String getPostfix() {
        return postfix;
    }

    public double getResult() {
        return result;
    }
}
