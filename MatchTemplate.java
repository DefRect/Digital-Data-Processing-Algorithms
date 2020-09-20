package FirstLabWork;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class MatchTemplate {

    private static final int LINE_THICKNESS = 7;

    public void recognitionObject(Mat img, Mat temp, String outputFile, int match_method, int rotate) {

        // Create the result matrix
        Mat result = createResultMatrix(img, temp);

        // Do the Matching and Normalize
        Imgproc.matchTemplate(img, temp, result, match_method);
        //Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());

        // Localizing the best match with minMaxLoc
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        Point matchLoc = mmr.maxLoc;

        // Show me what you got
        drawLine(img, matchLoc, matchLoc.x, matchLoc.y, matchLoc.x + temp.cols(), matchLoc.y, rotate);
        drawLine(img, matchLoc, matchLoc.x + temp.cols(), matchLoc.y, matchLoc.x + temp.cols(), matchLoc.y + temp.rows(), rotate);
        drawLine(img, matchLoc, matchLoc.x + temp.cols(), matchLoc.y + temp.rows(), matchLoc.x, matchLoc.y + temp.rows(), rotate);
        drawLine(img, matchLoc, matchLoc.x, matchLoc.y + temp.rows(), matchLoc.x, matchLoc.y, rotate);

        // Save the visualized detection.
        Imgcodecs.imwrite(outputFile, img);

    }

    private void drawLine(Mat img, Point center, double x_1, double y_1, double x_2, double y_2, int rotate) {

        double alpha = - (rotate * Math.PI) / 180.0;

        x_1 -= center.x;
        y_1 -= center.y;

        x_2 -= center.x;
        y_2 -= center.y;

        x_1 = x_1 * Math.cos(alpha) - y_1 * Math.sin(alpha);
        y_1 = x_1 * Math.sin(alpha) + y_1 * Math.cos(alpha);

        x_2 = x_2 * Math.cos(alpha) - y_2 * Math.sin(alpha);
        y_2 = x_2 * Math.sin(alpha) + y_2 * Math.cos(alpha);

        x_1 += center.x;
        y_1 += center.y;

        x_2 += center.x;
        y_2 += center.y;

        Imgproc.line(img, new Point(x_1, y_1), new Point(x_2, y_2), new Scalar(0, 0, 255), LINE_THICKNESS);
    }

    private Mat createResultMatrix(Mat img, Mat temp) {
        int result_cols = img.cols() - temp.cols() + 1;
        int result_rows = img.rows() - temp.rows() + 1;
        return new Mat(result_rows, result_cols, CvType.CV_32FC1);
    }
}
