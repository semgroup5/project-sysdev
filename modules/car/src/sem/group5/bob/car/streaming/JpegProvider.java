package sem.group5.bob.car.streaming;

/**
 * Created by jpp on 09/04/16.
 */
public interface JpegProvider {
    byte[] getLatestJpeg() throws Exception;
}
