import com.google.gson.annotations.SerializedName


data class Payload (

  @SerializedName("repository_id" ) var repositoryId : Int?               = null,
  @SerializedName("size"          ) var size         : Int?               = null,
  @SerializedName("distinct_size" ) var distinctSize : Int?               = null,
  @SerializedName("ref"           ) var ref          : String?            = null,
  @SerializedName("head"          ) var head         : String?            = null,
  @SerializedName("before"        ) var before       : String?            = null,
  @SerializedName("commits"       ) var commits      : ArrayList<Commits> = arrayListOf()

)