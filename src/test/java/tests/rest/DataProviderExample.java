package tests.rest;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class DataProviderExample {
    @Test(dataProvider="getData")
    public void instanceDbProvider(int p1, String p2) {
        System.out.println("Instance DataProvider Example: Data(" + p1 + ", " + p2 + ")");
    }
    @Test(dataProvider="getSingleElement")
    public void singleItemProvider(String p2) {
        System.out.println("Single item: " + p2);
    }

    @DataProvider
    public Object[][] getData() {
        return new Object[][]{{5, "five"}, {6, "six"}};
    }

    @DataProvider
    public Object[] getSingleElement() {
        return new String[] {"value returned by provider"};
    }
}
