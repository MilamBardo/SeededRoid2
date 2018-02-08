package interfaces;

import java.util.UUID;

import models.SeedType;

/**
 * Created by Talanath on 3/25/2017.
 */

public interface ISeed {

    long get_ID();

    SeedType getSeedType();

    void set_ID(long id);
}
