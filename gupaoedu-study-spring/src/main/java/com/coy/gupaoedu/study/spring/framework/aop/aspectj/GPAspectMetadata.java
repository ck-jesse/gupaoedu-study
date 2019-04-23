package com.coy.gupaoedu.study.spring.framework.aop.aspectj;

import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPPointcut;

import java.io.Serializable;

/**
 * Metadata for an AspectJ aspect class
 * AspectJ元数据
 *
 * @author chenck
 * @date 2019/4/23 17:42
 */
public class GPAspectMetadata implements Serializable {


    /**
     * The name of this aspect as defined to Spring (the bean name) -
     * allows us to determine if two pieces of advice come from the
     * same aspect and hence their relative precedence.
     */
    private final String aspectName;

    /**
     * The aspect class, stored separately for re-resolution of the
     * corresponding AjType on deserialization.
     */
    private final Class<?> aspectClass;

    /**
     * AspectJ reflection information (AspectJ 5 / Java 5 specific).
     * Re-resolved on deserialization since it isn't serializable itself.
     */
//    private transient AjType<?> ajType;

    /**
     * Spring AOP pointcut corresponding to the per clause of the
     * aspect. Will be the Pointcut.TRUE canonical instance in the
     * case of a singleton, otrwise an AspectJExpressionPointcut.
     */
    private final GPPointcut perClausePointcut;


    /**
     * Create a new AspectMetadata instance for the given aspect class.
     * @param aspectClass the aspect class
     * @param aspectName the name of the aspect
     */
    public GPAspectMetadata(Class<?> aspectClass, String aspectName) {
        this.aspectName = aspectName;

        Class<?> currClass = aspectClass;
//        AjType<?> ajType = null;
//        while (currClass != Object.class) {
//            AjType<?> ajTypeToCheck = AjTypeSystem.getAjType(currClass);
//            if (ajTypeToCheck.isAspect()) {
//                ajType = ajTypeToCheck;
//                break;
//            }
//            currClass = currClass.getSuperclass();
//        }
//        if (ajType == null) {
//            throw new IllegalArgumentException("Class '" + aspectClass.getName() + "' is not an @AspectJ aspect");
//        }
//        if (ajType.getDeclarePrecedence().length > 0) {
//            throw new IllegalArgumentException("DeclarePrecendence not presently supported in Spring AOP");
//        }
//        this.aspectClass = ajType.getJavaClass();
//        this.ajType = ajType;
//
//        switch (this.ajType.getPerClause().getKind()) {
//            case SINGLETON:
//                this.perClausePointcut = Pointcut.TRUE;
//                return;
//            case PERTARGET:
//            case PERTHIS:
//                AspectJExpressionPointcut ajexp = new AspectJExpressionPointcut();
//                ajexp.setLocation(aspectClass.getName());
//                ajexp.setExpression(findPerClause(aspectClass));
//                ajexp.setPointcutDeclarationScope(aspectClass);
//                this.perClausePointcut = ajexp;
//                return;
//            case PERTYPEWITHIN:
//                // Works with a type pattern
//                this.perClausePointcut = new ComposablePointcut(new TypePatternClassFilter(findPerClause(aspectClass)));
//                return;
//            default:
//                throw new AopConfigException(
//                        "PerClause " + ajType.getPerClause().getKind() + " not supported by Spring AOP for " + aspectClass);
//        }
    }

}
