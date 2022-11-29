package byow;
import java.io.File;
import java.io.Serializable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {

    // Source: Project 2 Utils.java source code
    public static File join(File first, String... others) {
        return Paths.get(first.getPath(), others).toFile();
    }

    public static byte[] serialize(Serializable obj) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(stream);
            objectStream.writeObject(obj);
            objectStream.close();
            return stream.toByteArray();
        } catch (IOException excp) {
            throw new RuntimeException("Internal error serializing commit.");
        }
    }

    public static void writeContents(File file, Object... contents) {
        try {
            if (file.isDirectory()) {
                throw
                        new IllegalArgumentException("cannot overwrite directory");
            }
            BufferedOutputStream str =
                    new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            for (Object obj : contents) {
                if (obj instanceof byte[]) {
                    str.write((byte[]) obj);
                } else {
                    str.write(((String) obj).getBytes(StandardCharsets.UTF_8));
                }
            }
            str.close();
        } catch (IOException | ClassCastException excp) {
            throw new IllegalArgumentException(excp);
        }
    }

    public static <T extends Serializable> T readObject(File file,
                                                 Class<T> expectedClass) {
        try {
            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream(file));
            T result = expectedClass.cast(in.readObject());
            in.close();
            return result;
        } catch (IOException | ClassCastException
                 | ClassNotFoundException excp) {
            throw new IllegalArgumentException(excp);
        }
    }

    public static boolean restrictedDelete(File file) {
        if (!file.isDirectory()) {
            return file.delete();
        } else {
            return false;
        }
    }
}
