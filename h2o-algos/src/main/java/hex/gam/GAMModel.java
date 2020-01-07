package hex.gam;

import hex.ModelMetrics;
import water.Key;
import java.util.Arrays;
import hex.Model;
import water.fvec.Frame;
import hex.ModelCategory;
import water.Keyed;
import water.Futures;
import hex.ModelMetricsSupervised;
import water.util.Log;


public class GAMModel extends Model<GAMModel, GAMModel.GAMParameters, GAMModel.GAMModelOutput> {
  
  @Override public ModelMetrics.MetricBuilder makeMetricBuilder(String[] domain) {
    return null;
    //return new MetricBuilderGAM(domain);
  }

  public GAMModel(Key<GAMModel> selfKey, GAMParameters parms, GAMModelOutput output) {
    super(selfKey, parms, output);
    assert(Arrays.equals(_key._kb, selfKey._kb));
  }

  ModelMetricsSupervised makeModelMetrics(Frame origFr, Frame adaptFr, String description) {
    Log.info("Making metrics: " + description);
    ModelMetrics.MetricBuilder mb = scoreMetrics(adaptFr);
    ModelMetricsSupervised mm = (ModelMetricsSupervised) mb.makeModelMetrics(this, origFr, adaptFr, null);
    mm._description = description;
    return mm;
  }
  

  @Override
  protected double[] score0(double[] data, double[] preds) {
    return new double[0];
  }

  @SuppressWarnings("WeakerAccess")
  public static class GAMParameters extends Model.Parameters {
    public String algoName() { return "GAM"; }
    public String fullName() { return "General Additive Model"; }
    public String javaName() { return GAMModel.class.getName(); }

    @Override
    public long progressUnits() {
      return 1;
    }

    public long _seed = -1;
  }
  
  public static class GAMModelOutput extends Model.Output {
    public Key<Frame> _alpha_key;
    
    GAMModelOutput(GAM b, Frame f, String[] respDomain) {
      super(b, f);
      _domains[_domains.length - 1] = respDomain != null ? respDomain : new String[]{"-1", "+1"};
    }

    @Override public ModelCategory getModelCategory() {
      return ModelCategory.Binomial;
    }
  }
  
  @Override
  protected Futures remove_impl(Futures fs, boolean cascade) {
    Keyed.remove(_output._alpha_key, fs, true);
    return super.remove_impl(fs, cascade);
  }
}
