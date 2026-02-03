package banking.application;

import banking.application.config.AppConfiguration;
import banking.application.console.OperationsConsoleListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BankingApplication {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);

        OperationsConsoleListener consoleListener = context
                .getBean(OperationsConsoleListener.class);

        consoleListener.run();
    }
}