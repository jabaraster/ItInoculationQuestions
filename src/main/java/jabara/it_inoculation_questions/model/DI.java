/**
 * 
 */
package jabara.it_inoculation_questions.model;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.jpa.util.SystemPropertyToPostgreJpaPropertiesParser;
import jabara.jpa_guice.SinglePersistenceUnitJpaModule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * @author jabaraster
 */
public final class DI {
    /**
     * 
     */
    public static final String    PERSISTENCE_UNIT_NAME = "mainPersistenceUnit"; //$NON-NLS-1$
    private static final Injector _injector             = createInjector();

    private DI() {
        // 　処理なし
    }

    /**
     * 指定のオブジェクトを取得します. <br>
     * このメソッドを使っていいのは次のクラスだけです. <br>
     * <ul>
     * <li>JAX-RSのリソースクラス</li>
     * </ul>
     * 
     * @param pType 取得するオブジェクトの型.
     * @return オブジェクト.
     * @param <T> 取得するオブジェクトの型.
     */
    public static <T> T get(final Class<T> pType) {
        ArgUtil.checkNull(pType, "pType"); //$NON-NLS-1$
        return _injector.getInstance(pType);
    }

    /**
     * @return Google Guiceの{@link Injector}を返します.
     */
    public static Injector getGuiceInjector() {
        return _injector;
    }

    static List<Method> getAnnotatedMethod(final Class<?> pScanTarget, final Class<? extends Annotation> pAnnotationType) {
        ArgUtil.checkNull(pScanTarget, "pScanTarget"); //$NON-NLS-1$
        ArgUtil.checkNull(pAnnotationType, "pAnnotationType"); //$NON-NLS-1$

        final List<Method> ret = new ArrayList<Method>();
        for (Class<?> type = pScanTarget; !Object.class.equals(type); type = type.getSuperclass()) {
            for (final Method method : type.getDeclaredMethods()) {
                if (method.isAnnotationPresent(pAnnotationType)) {
                    method.setAccessible(true);
                    ret.add(method);
                }
            }
        }
        return ret;
    }

    private static Injector createInjector() {
        final SinglePersistenceUnitJpaModule jpaModule = new SinglePersistenceUnitJpaModule(PERSISTENCE_UNIT_NAME,
                new SystemPropertyToPostgreJpaPropertiesParser());
        final Module m = new AbstractModule() {
            @SuppressWarnings("synthetic-access")
            @Override
            protected void configure() {
                final PostConstructAnnotatedMatcher matcher = new PostConstructAnnotatedMatcher();
                bindListener(matcher, new TypeListener() {
                    @Override
                    public <I> void hear(@SuppressWarnings("unused") final TypeLiteral<I> pType, final TypeEncounter<I> pEncounter) {
                        pEncounter.register(new PostConstructInjectionListener<I>());
                    }
                });
            }
        };
        return Guice.createInjector(jpaModule, m);
    }

    private static class PostConstructAnnotatedMatcher extends AbstractMatcher<TypeLiteral<?>> {

        private final ConcurrentMap<Class<?>, List<Method>> cache = new ConcurrentHashMap<Class<?>, List<Method>>();

        @Override
        public boolean matches(final TypeLiteral<?> pT) {
            @SuppressWarnings("unchecked")
            final Class<? extends Object> type = (Class<? extends Object>) pT.getType();
            List<Method> b = this.cache.get(type);
            if (b != null) {
                return !b.isEmpty();
            }
            b = getAnnotatedMethod(type, PostConstruct.class);
            this.cache.put(type, b);
            return !b.isEmpty();
        }
    }

    private static class PostConstructInjectionListener<I> implements InjectionListener<I> {
        /**
         * @see com.google.inject.spi.InjectionListener#afterInjection(java.lang.Object)
         */
        @Override
        public void afterInjection(final I pInjectee) {
            for (final Method postConstructMethod : getAnnotatedMethod(pInjectee.getClass(), PostConstruct.class)) {
                try {
                    postConstructMethod.invoke(pInjectee);
                } catch (final Exception e) {
                    throw ExceptionUtil.rethrow(e);
                }
            }
        }
    }
}
