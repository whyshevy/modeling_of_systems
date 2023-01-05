package ip91.bohdan.vakaliuk.config;

import ip91.bohdan.vakaliuk.model.GenType;

public class Config {

  public static final GenType GEN_TYPE = GenType.EXP;
  public static final int SEQ_SIZE = 10000;
  public static final int EXP_LAMBDA = 2;
  public static final int NORMAL_ALPHA = 3;
  public static final int NORMAL_SIGMA = 4;
  public static final double UNIFORM_A = Math.pow(5, 13);
  public static final double UNIFORM_C = Math.pow(2, 31);
  public static final int INTERVAL_COUNT = 20;

}
