package org.example;

public class Operation {
    private String data;
    private String corAcc;
    private String sum;
    private int dbt;
    private int cdt;


    public Operation(String data, String corAcc, String sum) {
        this.data = data;
        this.corAcc = corAcc;
        this.sum = sum;

        System.out.println(data + corAcc + sum);
    }

    public String getData() {
        return data;
    }

    public String getCorAcc() {
        return corAcc;
    }

    public String getSum() {
        return sum;
    }
}
