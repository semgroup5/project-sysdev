package sem.group5.bob.car.streaming;

/**
 * Interface to build the jpeg or depth provider.
 */
interface JpegProvider {
    byte[] getLatestJpeg() throws Exception;
}
