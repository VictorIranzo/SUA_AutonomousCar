package sua.autonomouscar.infrastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;


public final class OSGiUtils {
	
	public static <C> C getService(BundleContext context, Class<C> clase) {
		ServiceReference<C> ref = context.getServiceReference(clase);
		if ( ref == null ) return null;
		return context.getService(ref);
	}

	public static <C> C getService(BundleContext context, Class<C> clase, String filter) {
		Collection<ServiceReference<C>> refs = null;
		try {
			refs = context.getServiceReferences(clase, filter);
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
			return null;
		}

		if ( refs == null || refs.size() == 0 )
			return null;
		
		return context.getService(refs.iterator().next());

	}

	public static <C> List<C> getServices(BundleContext context, Class<C> clase) {
		return getServices(context, clase, null);
	}
	
	public static <C> List<C> getServices(BundleContext context, Class<C> clase, String filter) {

		Collection<ServiceReference<C>> refs = null;
		try {
			refs = context.getServiceReferences(clase, filter);
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
			return null;
		}
		
		List<C> list = new ArrayList<C>();
		for(ServiceReference<C> ref : refs )
			list.add(context.getService(ref));
		
		return list;
	}

}
