package sem.group5.bob.car;

import org.libjpegturbo.turbojpeg.TJ;
import org.libjpegturbo.turbojpeg.TJCompressor;
import org.openkinect.freenect.FrameMode;

import java.nio.ByteBuffer;
import java.util.Observable;

public class ComboJpegProvider extends Observable implements JpegProvider {
    static ByteBuffer latestVideoFrame;
    static ByteBuffer latestDepthFrame;

    public void receiveVideo(FrameMode frameMode, ByteBuffer byteBuffer, int i) {
        latestVideoFrame = byteBuffer;
    }
    public void receiveDepth(FrameMode frameMode, ByteBuffer byteBuffer, int i) {
        latestDepthFrame = byteBuffer;
    }

    int pixelWidth = 1;
    int imageSize = 640 * 480 * pixelWidth;
    byte[] comboFrame = new byte[imageSize];
    public byte[] getLatestJpeg() throws Exception{
        if(latestDepthFrame == null)
            Thread.sleep(1000);

        int videoFrameSize = 640*480*2;
        byte[] vFrame = new byte[videoFrameSize];
        ByteBuffer lvf = latestVideoFrame;
        lvf.rewind();
        lvf.get(vFrame);

        int depthFrameSize = 640*480*2;
        byte[] dFrame = new byte[depthFrameSize];
        ByteBuffer ldf = latestDepthFrame;
        ldf.rewind();
        ldf.get(dFrame);

        System.out.println("Making Frame");

        for (int i =0; i < imageSize; i = i + pixelWidth) {
            int pixel = (i / pixelWidth) * 2; // 2 bytes per pixel for both depth and video
            comboFrame[i + 0] =  (byte)( ( ( dFrame[pixel +1] & 0xFF ) << 6) | ( dFrame[pixel+0] & 0xFF >>> 3)  ); // squish depth
            comboFrame[i + 1] = vFrame[pixel];
            comboFrame[i + 2] = vFrame[pixel + 1];
        }

        System.out.println("Compressing Frame");

        try {
            TJCompressor tjc = new TJCompressor();

            tjc.setJPEGQuality(20);
            tjc.setSubsamp(TJ.SAMP_GRAY);
            tjc.setSourceImage(comboFrame, 640, (640*pixelWidth), 480, TJ.PF_RGB);

            int flags = 0;
            byte[] compressed = tjc.compress(flags);
            byte[] compressedTruncated = new byte[tjc.getCompressedSize()];
            System.arraycopy(compressed, 0, compressedTruncated, 0, tjc.getCompressedSize());
            System.out.println("Sending Frame");
            return compressedTruncated;
        }
        catch (Exception e) {
            System.err.println("Exception caught, message: " + e.getMessage());
            throw e;
        }
    }
}
