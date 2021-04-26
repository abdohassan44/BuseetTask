package  com.example.buseettask.network

import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

@Keep
data class ApiResponse(

        @SerializedName("meta") val meta: Meta,
        @SerializedName("response") val response: Response
)
@Keep
data class Response(

        @SerializedName("venues") val venues: List<Venues>,
        @SerializedName("confident") val confident: Boolean
)
@Keep
data class Meta(

        @SerializedName("code") val code: Int,
        @SerializedName("requestId") val requestId: String
)
@Keep
data class Venues(

        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("location") val location: Location,
        @SerializedName("verified") val verified: Boolean,
        @SerializedName("referralId") val referralId: String,
        @SerializedName("hasPerk") val hasPerk: Boolean,
        @SerializedName("distance") val distance: Int

)
{
        companion object DIFFUtil : DiffUtil.ItemCallback<Venues>() {
                override fun areContentsTheSame(
                        oldItem: Venues,
                        newItem: Venues
                ): Boolean {
                        return oldItem == newItem
                }

                override fun areItemsTheSame(
                        oldItem: Venues,
                        newItem: Venues
                ): Boolean {
                        return oldItem == newItem
                }
        }
}
@Keep
data class Location(

        @SerializedName("address") val address: String? = "",
        @SerializedName("lat") val lat: Double,
        @SerializedName("lng") val lng: Double,
        @SerializedName("distance") val distance: Int,
)