package de.eldritch.anura.data;

import de.eldritch.anura.Anura;
import de.eldritch.anura.Instance;

/**
 * The database connection to MySQL. This class is only instantiated once by {@link Anura} and provides a connection for
 * all instances.
 * <p>This class should <b>always</b> be thread-safe as multiple {@link Instance} objects may call it simultaneously.
 */
public class DataService {

}
