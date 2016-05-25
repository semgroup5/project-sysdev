package sem.group5.bob.car.streaming;

import org.libjpegturbo.turbojpeg.TJ;
import org.libjpegturbo.turbojpeg.TJCompressor;
import org.openkinect.freenect.FrameMode;
import sun.misc.Unsafe;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Observable;


/**
 *
 * @see java.util.Observable
 * [JpegProvider]{@link sem.group5.bob.car.streaming.JpegProvider}
 */
public class DepthJpegProvider extends Observable implements JpegProvider
{
    private static ByteBuffer latestDepthFrame;
    private static boolean processingDepth = false;
    private int pixelWidth = 1;
    private int imageSize = 640 * 480 * pixelWidth;
    private byte[] comboFrame = new byte[imageSize];

    /**
     *
     */
    public DepthJpegProvider()
    {
        try
        {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
        }catch(Exception ignored){}
    }

    /**
     * todo
     * @param frameMode frame
     * @param byteBuffer byte
     * @param i int
     */
    public void receiveDepth(FrameMode frameMode, ByteBuffer byteBuffer, int i)
    {
        if (!processingDepth){ latestDepthFrame = byteBuffer;}
    }

    /**
     * todo
     * @return frame
     * @throws Exception
     */
    public byte[] getLatestJpeg() throws Exception
    {
        while(latestDepthFrame == null)
        {
            Thread.sleep(2*1000);
        }

        int depthFrameSize = (640*480*2);
        byte[] dFrame = new byte[depthFrameSize];
        processingDepth = true;
        ShortBuffer ldf = latestDepthFrame.asShortBuffer();
        ldf.rewind();
        short[] depths = new short[640*480];
        ldf.get(depths);

        System.out.println("Making Frame");
        for(int i = 0; i < 640*480; i++)
        {
            comboFrame[i] =(byte)(depths[i]/16);
        }

        processingDepth = false;
        System.out.println("Compressing Frame");

        try
        {
            TJCompressor tjc = new TJCompressor();

            tjc.setJPEGQuality(20);
            tjc.setSubsamp(TJ.SAMP_GRAY);
            tjc.setSourceImage(comboFrame, 640, (640*pixelWidth), 480, TJ.PF_GRAY);

            int flags = 0;
            byte[] compressed = tjc.compress(flags);
            byte[] compressedTruncated = new byte[tjc.getCompressedSize()];
            System.arraycopy(compressed, 0, compressedTruncated, 0, tjc.getCompressedSize());
            System.out.println("Sending Frame");
            return compressedTruncated;
        }
        catch (Exception ignore) {}
        return dFrame;
    }
}