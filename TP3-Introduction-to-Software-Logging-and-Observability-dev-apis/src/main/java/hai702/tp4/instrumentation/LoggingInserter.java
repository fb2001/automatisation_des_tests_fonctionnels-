package hai702.tp4.instrumentation;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.filter.TypeFilter;

public class LoggingInserter {
    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        launcher.addInputResource("src/main/java");
        launcher.setSourceOutputDirectory("instrumented-src");

        CtModel model = launcher.buildModel();

        model.getElements(new TypeFilter<>(CtMethod.class)).forEach(method -> {
            String className = method.getDeclaringType().getQualifiedName();

            // On ne log que les méthodes des services
            if (!className.contains("Service")) {
                return;
            }

            Factory factory = method.getFactory();
            String methodName = method.getSimpleName();

            // Construire le log avec les paramètres
            StringBuilder logCode = new StringBuilder();
            logCode.append("org.slf4j.LoggerFactory.getLogger(")
                    .append(className).append(".class).info(\"")
                    .append("ACTION: ").append(methodName);

            // Ajouter les paramètres au log
            for (Object obj : method.getParameters()) {
                CtParameter<?> param = (CtParameter<?>) obj;
                logCode.append(" | ").append(param.getSimpleName())
                        .append("={}\", ").append(param.getSimpleName());
            }


            if (method.getParameters().isEmpty()) {
                logCode.append("\")");
            } else {
                logCode.append(")");
            }

            CtCodeSnippetStatement logStatement =
                    factory.Code().createCodeSnippetStatement(logCode.toString());

            if (method.getBody() != null) {
                method.getBody().insertBegin(logStatement);
            }
        });

        launcher.prettyprint();
    }
}