package npx.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import npx.cisco.parser.CiscoParser;
import npx.netmodel.config.PassiveConfig;
import npx.netmodel.render.GraphYedRenderer;
import org.apache.log4j.Logger;

/**
 *
 * @author Luis Dias Costa
 */
public class CiscoParserDemo {

    public static void main(String[] argv) {
        new CiscoParserDemo().Launch(argv);
    }

    private void Launch(String[] argv) {

        if (argv.length != 1) {
            System.err.println("First argument must be a valid Cisco IOS file or directory containing .ios files");
            System.exit(1);
        }

        File file = new File(argv[0]);
        CiscoParser parser = new CiscoParser();
        ArrayList<PassiveConfig> configs = new ArrayList<PassiveConfig>();
        if (file.isFile() && file.canRead()) {
            System.out.print("Started file parser...");
            PassiveConfig cfg = parser.parse(argv[0]);
            configs.add(cfg);
        } else if (file.isDirectory()) {
            System.out.print("Started directory parser...");
            configs = parser.parseDir(argv[0], ".ios");
        }
        System.out.println(" done.");

        System.out.print("Started yEd graph rendering...");
        GraphYedRenderer gyr = new GraphYedRenderer(configs);
        String out = gyr.getResults();
        
        try {
        	FileOutputStream f = new FileOutputStream("yed.graphml");
            f.write(out.getBytes());
            f.close();
        } catch (IOException ex) {
            logger.error("error writing graphml to file: " + ex);
        } 
        System.out.println(" done.");

    }
    private final Logger logger = Logger.getLogger(this.getClass());
}
