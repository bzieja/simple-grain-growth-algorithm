package gui;

import app.DependencyContainer;
import app.AppConfiguration;

import java.lang.reflect.InvocationTargetException;

public class TestClass {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        AppConfiguration instance = DependencyContainer.getInstance(AppConfiguration.class);
        instance.setStartWithInclusions(true);

        AppConfiguration instanceSecond = DependencyContainer.getInstance(AppConfiguration.class);
        System.out.println(instanceSecond.isStartWithInclusions());
    }
}
