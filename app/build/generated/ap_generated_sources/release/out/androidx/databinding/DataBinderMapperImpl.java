package androidx.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new com.pindergarten.pindergarten_android.DataBinderMapperImpl());
  }
}
