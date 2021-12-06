package androidx.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new com.pindergarten_android.pindergarten_android.DataBinderMapperImpl());
  }
}
