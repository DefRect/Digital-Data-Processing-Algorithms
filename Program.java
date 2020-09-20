package FirstLabWork;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Program {

    final double LEFT_BORDER_SCALE = 0.9;
    final double RIGHT_BORDER_SCALE = 1.1;
    final double STEP_SCALE = 0.025;

    final int LEFT_BORDER_ROTATE = -10;
    final int RIGHT_BORDER_ROTATE = 10;
    final int STEP_ROTATE = 2;

    private Mat img = new Mat();
    private Mat temp = new Mat();

    public void run(String inputFileMain, String inputFileTemp, String outputFile, int match_method){

        searchInOriginalImages(inputFileMain, inputFileTemp, outputFile + "\\Original.png", match_method);
        scalingImage(inputFileMain, inputFileTemp, outputFile, match_method);
        rotateImage(inputFileMain, inputFileTemp, outputFile, match_method);

    }

    private void searchInOriginalImages(String inputFileMain, String inputFileTemp, String outputFile, int match_method) {
        System.out.println("Start searchInOriginalImages...");

        loadImg(inputFileMain, inputFileTemp);

        MatchTemplate search = new MatchTemplate();
        search.recognitionObject(img, temp, outputFile, match_method, 0);

        System.out.println("End searchInOriginalImages...");
    }

    private void loadImg(String inputFileMain, String inputFileTemp) {
        img = Imgcodecs.imread(inputFileMain);
        temp = Imgcodecs.imread(inputFileTemp);
    }

    private void scalingImage(String inputFileMain, String inputFileTemp,String outputFile, int match_method) {
        System.out.println("Start scalingImage...");

        Mat resizeImg = new Mat();
        Mat resizeTemp = new Mat();

        for (double scale = LEFT_BORDER_SCALE; scale <= RIGHT_BORDER_SCALE; scale += STEP_SCALE) {
            scale = getScale(scale, 3);

            System.out.println("Processing an image with a scaling value of " + scale);

            loadImg(inputFileMain, inputFileTemp);

            resizeImage(img, resizeImg, scale);
            resizeImage(temp, resizeTemp, scale);

            String outputFileNew = outputFile + "\\Scale" + scale + ".png";
            MatchTemplate search = new MatchTemplate();
            search.recognitionObject(resizeImg, resizeTemp, outputFileNew, match_method, 0);
        }

        System.out.println("End scalingImage...");
    }

    private double getScale(double scale, int places) {
        double tmp = Math.pow(10, places);
        return Math.round(scale * tmp) / tmp;
    }


    private void resizeImage(Mat oldImg, Mat newImg, double scale) {
        Size size = new Size();
        size.height = oldImg.height() * scale;
        size.width = oldImg.width() * scale;
        Imgproc.resize(oldImg, newImg, size);
    }

    private void rotateImage(String inputFileMain, String inputFileTemp,String outputFile, int match_method) {
        System.out.println("Start rotateImage...");

        for (int rotate = LEFT_BORDER_ROTATE; rotate <= RIGHT_BORDER_ROTATE; rotate += STEP_ROTATE) {
            System.out.println("Image processing with rotation value " + rotate);

            loadImg(inputFileMain, inputFileTemp);

            // Трансформация вращения
            Mat imgM = Imgproc.getRotationMatrix2D(new Point(img.width() / 2, img.height() / 2), rotate, 1);
            Mat tempM = Imgproc.getRotationMatrix2D(new Point(temp.width() / 2, temp.height() / 2), rotate, 1);

            // Расчет размеров и положения
            Rect rectImg = new RotatedRect(new Point(img.width() / 2, img.height() / 2), new Size(img.width(), img.height()), rotate).boundingRect();
            Rect rectTemp = new RotatedRect(new Point(temp.width() / 2, temp.height() / 2), new Size(temp.width(), temp.height()), rotate).boundingRect();

            // Корректировка матрицы трансформации
            сorrectionMatrix(imgM, rectImg);
            сorrectionMatrix(tempM, rectTemp);

            // Трансформация
            Mat img1 = new Mat();
            Mat img2 = new Mat();

            Imgproc.warpAffine(img, img1, imgM, rectImg.size(), Imgproc.INTER_LINEAR, Core.BORDER_CONSTANT, new Scalar(255, 255, 255, 255));
            Imgproc.warpAffine(temp, img2, tempM, rectTemp.size(), Imgproc.INTER_LINEAR, Core.BORDER_CONSTANT, new Scalar(255, 255, 255, 255));

            String outputFileNew = outputFile + "\\Rotate" + rotate + ".png";

            MatchTemplate search = new MatchTemplate();
            search.recognitionObject(img1, img2, outputFileNew, match_method, rotate);
        }

        System.out.println("End rotateImage...");
    }

    private void сorrectionMatrix(Mat imgM, Rect rectImg) {
        double[] arrXImg = imgM.get(0, 2);
        double[] arrYImg = imgM.get(1, 2);
        arrXImg[0] -= rectImg.x;
        arrYImg[0] -= rectImg.y;
        imgM.put(0, 2, arrXImg);
        imgM.put(1, 2, arrYImg);
    }

}
