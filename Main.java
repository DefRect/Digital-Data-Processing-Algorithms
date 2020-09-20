package FirstLabWork;

import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;

public class Main {

    static final String INPUT_FILE_MAIN = "C:\\Users\\Nikita\\Desktop\\Files for programs\\Images\\main.png";
    static final String INPUT_FILE_OWN = "C:\\Users\\Nikita\\Desktop\\Files for programs\\Images\\own_16.png";
    static final String INPUT_FILE_FOREIGN = "C:\\Users\\Nikita\\Desktop\\Files for programs\\Images\\foreign_16.png";
    static final String OUTPUT_FILE_OWN = "C:\\Users\\Nikita\\Desktop\\Files for programs\\Images\\outputOwn";
    static final String OUTPUT_FILE_FOREIGN = "C:\\Users\\Nikita\\Desktop\\Files for programs\\Images\\outputForeign";

    public static void main(String[] args) {

        // load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Program program = new Program();
        System.out.println("OWN!!!");
        program.run(INPUT_FILE_MAIN, INPUT_FILE_OWN, OUTPUT_FILE_OWN, Imgproc.TM_CCORR_NORMED);
        System.out.println("FOREIGN!!!");
        program.run(INPUT_FILE_MAIN, INPUT_FILE_FOREIGN, OUTPUT_FILE_FOREIGN, Imgproc.TM_CCORR_NORMED);

    }
}
