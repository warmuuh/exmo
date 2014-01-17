package wrm.exmo.transform.xml;

import java.io.InputStream;
import java.net.URI;

import lombok.extern.apachecommons.CommonsLog;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

@CommonsLog
final class IncludeEntityResolver implements
			EntityResolver {
		public InputSource resolveEntity(String publicId, String systemId) {
			try {
				
//					System.out.println("Entity resolving systemID: " + systemId + "  publicId: " + publicId);
					URI url = new URI(systemId);
					if (!url.getScheme().equals("classpath"))
						throw new RuntimeException("Unsupported protocoll: " + url.getScheme());
					
					
					
					InputStream stream = getClass().getResourceAsStream(url.getPath());
					if (stream == null)
						throw new RuntimeException("Cannot find file: " + url.getPath());

					log.trace("Entity Resolved: " + systemId);
					return new InputSource(stream);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}