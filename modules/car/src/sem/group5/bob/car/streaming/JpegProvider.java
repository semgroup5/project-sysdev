package sem.group5.bob.car.streaming;

interface JpegProvider {
    byte[] getLatestJpeg() throws Exception;
}
