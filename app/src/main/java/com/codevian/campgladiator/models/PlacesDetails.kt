package com.codevian.campgladiator.models

data class PlacesDetails(
    val data: List<Data>,
    val message: String,
    val success: Boolean
)

data class Data(
    val distance: String,
    val location: List<Location>,
    val placeActive: String,
    val placeAdditionalInfo: String,
    val placeAddress1: String,
    val placeAddress2: String,
    val placeCity: String,
    val placeCountry: String,
    val placeDesc: String,
    val placeID: String,
    val placeLatitude: String,
    val placeLongitude: String,
    val placeName: String,
    val placeState: String,
    val placeZipcode: String,
    val regionID: String,
    val subRegionID: String
)

data class Location(
    val ID: String,
    val approvedDate: Any,
    val isApproved: String,
    val lastSubmissionDate: Any,
    val locationActive: String,
    val locationAddress1: Any,
    val locationAddress2: Any,
    val locationAge: String,
    val locationAlternate: String,
    val locationAvgRating: Double,
    val locationCity: Any,
    val locationDateEntered: String,
    val locationDay: String,
    val locationDesc: String,
    val locationEndDate: Any,
    val locationEndHour: String,
    val locationEndMeetDate: Any,
    val locationEndMeridiem: String,
    val locationEndMin: String,
    val locationEndRegistrationDate: Any,
    val locationFullAddress: String,
    val locationFullDesc: Any,
    val locationID: String,
    val locationInactiveDate: Any,
    val locationIsVisible: String,
    val locationLatitude: String,
    val locationLongitude: String,
    val locationModifiedDate: String,
    val locationName: String,
    val locationOriginalStartDate: String,
    val locationStartDate: String,
    val locationStartHour: String,
    val locationStartMeetDate: String,
    val locationStartMeridiem: String,
    val locationStartMin: String,
    val locationStartRegistrationDate: String,
    val locationTime: String,
    val locationZipcode: Any,
    val originalSubmissionDate: Any,
    val parentLocationID: String,
    val placeID: String,
    val regionID: String,
    val rootParentLocationID: String,
    val trainerID: String,
    val trainers: List<Trainer>,
    val underPerformingCount: String
)

data class Trainer(
    val adminUserEmail: String,
    val adminUserFirstname: String,
    val adminUserID: String,
    val adminUserLastname: String,
    val adminUserPhone: String,
    val adminUserPhoto: String,
    val locationSplitPerc: String
)