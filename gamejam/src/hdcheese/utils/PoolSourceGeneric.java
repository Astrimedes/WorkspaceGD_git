package hdcheese.utils;

import java.util.HashMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * A 'Pool of Pools' - {@link Pool}s can be created programatically and then referred to by object type for obtain() and free()
 * @author mturano
 *
 */
public class PoolSourceGeneric {
	
		// hashmap of all pools
		private static HashMap<Class<? extends Poolable>, Pool<Poolable>> pools = new HashMap<Class<? extends Poolable>, Pool<Poolable>>();
		
		/**
		 * Create a new {@link Pool} of type <code>T</code>, with initial count of <code>start</code> and maximum count of <code>max</code>
		 * @param T pooled object class
		 * @param start starting count of pool
		 * @param max max expandable count of pool
		 */
		@SuppressWarnings("unchecked")
		public static <T> void allocatePool(final Class<? extends Poolable> T, int start, int max) {
			Pool<T> newPool = new Pool<T>(start, max) {
				@Override
				protected T newObject() {
					try {
						return (T)T.newInstance();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			};
			
			pools.put(T, (Pool<Poolable>)newPool);
		}
		
		/**
		 * Get a desired {@link Poolable} object
		 * @param T Object's class
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public static <T> T obtain(Class<? extends Poolable> T) {
			return (T)pools.get(T).obtain();
		}
		
		/**
		 * Return an object to it's {@link Pool}
		 * @param object Pooled object to be returned
		 */
		public static void free(Poolable object) {
			pools.get(object.getClass()).free(object);
		}
		
		/**
		 * Remove all free objects from the {@link Pool}s
		 */
		public static void clearAll() {
			for(Pool<Poolable> p : pools.values()) {
				p.clear();
			}
			pools.clear();
		}
}
