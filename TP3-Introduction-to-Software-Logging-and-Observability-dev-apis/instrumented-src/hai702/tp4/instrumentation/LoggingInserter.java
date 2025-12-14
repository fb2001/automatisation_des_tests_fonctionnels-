package hai702.tp4.instrumentation;
public class LoggingInserter {
    public static void main(java.lang.String[] args) {
        spoon.Launcher launcher = new spoon.Launcher();
        launcher.addInputResource("src/main/java");
        launcher.setSourceOutputDirectory("instrumented-src");
        spoon.reflect.CtModel model = launcher.buildModel();
        model.getElements(new spoon.reflect.visitor.filter.TypeFilter<>(spoon.reflect.declaration.CtMethod.class)).forEach(method -> {
            java.lang.String className = method.getDeclaringType().getQualifiedName();
            // On ne log que les méthodes des services
            if (!className.contains("Service")) {
                return;
            }
            spoon.reflect.factory.Factory factory = method.getFactory();
            java.lang.String methodName = method.getSimpleName();
            // Construire le log avec les paramètres
            java.lang.StringBuilder logCode = new java.lang.StringBuilder();
            logCode.append("org.slf4j.LoggerFactory.getLogger(").append(className).append(".class).info(\"").append("ACTION: ").append(methodName);
            // Ajouter les paramètres au log
            for (java.lang.Object obj : method.getParameters()) {
                CtParameter<?> param = ((CtParameter<?>) (obj));
                logCode.append(" | ").append(param.getSimpleName()).append("={}\", ").append(param.getSimpleName());
            }
            if (method.getParameters().isEmpty()) {
                logCode.append("\")");
            } else {
                logCode.append(")");
            }
            spoon.reflect.code.CtCodeSnippetStatement logStatement = factory.Code().createCodeSnippetStatement(logCode.toString());
            if (method.getBody() != null) {
                method.getBody().insertBegin(logStatement);
            }
        });
        launcher.prettyprint();
    }
}