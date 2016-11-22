package methods.lda;

import cc.mallet.types.Instance;
import entities.Song;
import entities.Word;

public class Song2InstanceTransformer {

    public static Instance transform(Song song) {

        StringBuilder sb = new StringBuilder();

        for (Word w : song.lyrics) {
            for (int i = 0; i < w.count; i++) {
                sb.append(w.word);
                sb.append(' ');
            }
        }

        sb.deleteCharAt(sb.length()-1);

        return new Instance(sb.toString(), String.valueOf(song.getId()), String.valueOf(song.getId()), null);
    }
}
