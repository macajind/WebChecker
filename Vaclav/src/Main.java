import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

    public static void main(String[] args) {
        URL u;
        InputStream is = null;
        DataInputStream dis;
        String s;

        try {

            //------------------------------------------------------------//
            // Step 2:  Create the URL.                                   //
            //------------------------------------------------------------//
            // Note: Put your real URL here, or better yet, read it as a  //
            // command-line arg, or read it from a file.                  //
            //------------------------------------------------------------//

            u = new URL("http://www.imdb.com/title/tt2209764/?ref_=nv_sr_3");

            //----------------------------------------------//
            // Step 3:  Open an input stream from the url.  //
            //----------------------------------------------//

            is = u.openStream();         // throws an IOException

            //-------------------------------------------------------------//
            // Step 4:                                                     //
            //-------------------------------------------------------------//
            // Convert the InputStream to a buffered DataInputStream.      //
            // Buffering the stream makes the reading faster; the          //
            // readLine() method of the DataInputStream makes the reading  //
            // easier.                                                     //
            //-------------------------------------------------------------//

            dis = new DataInputStream(new BufferedInputStream(is));

            //------------------------------------------------------------//
            // Step 5:                                                    //
            //------------------------------------------------------------//
            // Now just read each record of the input stream, and print   //
            // it out.  Note that it's assumed that this problem is run   //
            // from a command-line, not from an application or applet.    //
            //------------------------------------------------------------//

            while ((s = dis.readLine()) != null) {
                System.out.println(s);
            }

        } catch (MalformedURLException mue) {

            System.out.println("Ouch - a MalformedURLException happened.");
            mue.printStackTrace();
            System.exit(1);

        } catch (IOException ioe) {

            System.out.println("Oops- an IOException happened.");
            ioe.printStackTrace();
            System.exit(1);

        } finally {

            //---------------------------------//
            // Step 6:  Close the InputStream  //
            //---------------------------------//

            try {
                is.close();
            } catch (IOException ioe) {
                // just going to ignore this one
            }
        }

        System.out.printf("Name: \n");
        System.out.printf("Genres: \n" ); //zanr
        System.out.printf("Release Date: \n");
        System.out.printf("Country: \n");
        System.out.printf("Runtime: \n");
        System.out.printf("Storyline: \n");
        System.out.printf("Ratings: \n");
        System.out.printf("Videos: \n");
        System.out.printf("Photos \n");
    }
}
