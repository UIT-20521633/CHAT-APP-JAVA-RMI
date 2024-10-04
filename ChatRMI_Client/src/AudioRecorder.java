import javax.sound.sampled.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AudioRecorder {

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public byte[] recordAudio(int durationInSeconds) {
        AudioFormat format = getAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line not supported");
            return null;
        }

        try (TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            targetLine.open(format);
            targetLine.start();

            byte[] buffer = new byte[4096];
            int bytesRead;

            long end = System.currentTimeMillis() + (durationInSeconds * 1000);
            while (System.currentTimeMillis() < end) {
                bytesRead = targetLine.read(buffer, 0, buffer.length);
                out.write(buffer, 0, bytesRead);
            }

            targetLine.stop();
            targetLine.close();

            byte[] audioData = out.toByteArray();
            ByteArrayOutputStream waveOut = new ByteArrayOutputStream();
            try (AudioInputStream audioInputStream = new AudioInputStream(
                    new ByteArrayInputStream(audioData), format, audioData.length / format.getFrameSize())) {
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, waveOut);
            }
            return waveOut.toByteArray();
        } catch (LineUnavailableException | IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
