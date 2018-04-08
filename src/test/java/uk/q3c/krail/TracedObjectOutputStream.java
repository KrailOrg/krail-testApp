package uk.q3c.krail;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by David Sowerby on 01 Apr 2018
 */
public class TracedObjectOutputStream extends ObjectOutputStream {

    protected TracedObjectOutputStream() throws IOException, SecurityException {
        super();

    }
}
