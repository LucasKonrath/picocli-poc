import com.google.gson.annotations.SerializedName


data class Commits (

  @SerializedName("sha"      ) var sha      : String?  = null,
  @SerializedName("author"   ) var author   : Author?  = Author(),
  @SerializedName("message"  ) var message  : String?  = null,
  @SerializedName("distinct" ) var distinct : Boolean? = null,
  @SerializedName("url"      ) var url      : String?  = null

)