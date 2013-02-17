import jabara.jetty_memcached.MemcachedSessionServerStarter;

/**
 * 
 */

/**
 * 
 * @author jabaraster
 */
public class ItInoculationQuestionsWebStarter {
    /**
     * @param pArgs 起動引数.
     * @throws Throwable -
     */
    public static void main(final String[] pArgs) throws Throwable {
        try {
            new MemcachedSessionServerStarter().start();
        } catch (final Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }
}
