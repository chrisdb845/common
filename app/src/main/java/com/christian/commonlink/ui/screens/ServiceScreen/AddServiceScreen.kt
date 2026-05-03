package com.christian.commonlink.ui.screens.ServiceScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.christian.commonlink.ui.screens.services.Service
import com.christian.commonlink.ui.screens.services.ServiceViewModel

private val DeepIndigo   = Color(0xFF1A1040)
private val BgColor      = Color(0xFFF5F3FB)
private val SubtitleGray = Color(0xFF888888)
private val ErrorRed     = Color(0xFFE53935)
private val TealColor    = Color(0xFF00695C)

@Composable
fun ServiceTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isError: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Column {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DeepIndigo,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(text = placeholder, fontSize = 13.sp, color = SubtitleGray)
            },
            leadingIcon = leadingIcon,
            singleLine = singleLine,
            maxLines = maxLines,
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(
                    width = 1.5.dp,
                    color = if (isError) ErrorRed
                    else if (value.isNotEmpty()) TealColor
                    else Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(14.dp)
                ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(14.dp)
        )
        if (isError) {
            Text(
                text = "This field is required",
                fontSize = 11.sp,
                color = ErrorRed,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun ServiceCategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable { onClick() }
            .background(
                color = if (isSelected) TealColor else Color.White,
                shape = RoundedCornerShape(50)
            )
            .border(
                width = 1.5.dp,
                color = if (isSelected) TealColor else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 16.dp, vertical = 9.dp)
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else DeepIndigo
        )
    }
}

@Composable
fun AddServiceScreen(
    navController: NavController,
    viewModel: ServiceViewModel = viewModel()
) {
    var title       by remember { mutableStateOf("") }
    var provider    by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location    by remember { mutableStateOf("") }
    var phone       by remember { mutableStateOf("") }
    var rating      by remember { mutableStateOf("") }
    var category    by remember { mutableStateOf("Plumbing") }

    var titleError       by remember { mutableStateOf(false) }
    var providerError    by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var locationError    by remember { mutableStateOf(false) }
    var phoneError       by remember { mutableStateOf(false) }
    var ratingError      by remember { mutableStateOf(false) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    val serviceCategories = listOf(
        "Plumbing", "Electric", "Cleaning",
        "Carpentry", "Painting", "Security"
    )

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "🎉", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Service Listed!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = DeepIndigo
                    )
                }
            },
            text = {
                Text(
                    text = "Your service has been successfully listed on the community board.",
                    fontSize = 14.sp,
                    color = SubtitleGray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TealColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Services", color = Color.White)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
    ) {

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF004D40), TealColor)
                    )
                )
                .padding(start = 8.dp, top = 48.dp, end = 20.dp, bottom = 28.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "List a Service",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "  Fill in your service details below",
                    fontSize = 13.sp,
                    color = Color(0xCCFFFFFF)
                )
            }
        }

        // Form
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            ServiceTextField(
                value = title,
                onValueChange = { title = it; titleError = false },
                label = "Service Title *",
                placeholder = "e.g. Plumbing Services",
                isError = titleError
            )

            ServiceTextField(
                value = provider,
                onValueChange = { provider = it; providerError = false },
                label = "Your Name *",
                placeholder = "e.g. John Kamau",
                isError = providerError,
                leadingIcon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Provider",
                        tint = if (providerError) ErrorRed else TealColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            ServiceTextField(
                value = description,
                onValueChange = { description = it; descriptionError = false },
                label = "Description *",
                placeholder = "Describe your service...",
                isError = descriptionError,
                singleLine = false,
                maxLines = 4
            )

            ServiceTextField(
                value = location,
                onValueChange = { location = it; locationError = false },
                label = "Location *",
                placeholder = "e.g. Nairobi Wide",
                isError = locationError,
                leadingIcon = {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = if (locationError) ErrorRed else TealColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            ServiceTextField(
                value = phone,
                onValueChange = { phone = it; phoneError = false },
                label = "Phone Number *",
                placeholder = "e.g. 0712345678",
                isError = phoneError,
                leadingIcon = {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = "Phone",
                        tint = if (phoneError) ErrorRed else TealColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            ServiceTextField(
                value = rating,
                onValueChange = { rating = it; ratingError = false },
                label = "Your Rating (1.0 - 5.0) *",
                placeholder = "e.g. 4.8",
                isError = ratingError,
                leadingIcon = {
                    Text(
                        text = "⭐",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            )

            // Category chips
            Column {
                Text(
                    text = "Category",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepIndigo,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    serviceCategories.take(3).forEach { cat ->
                        ServiceCategoryChip(
                            label = cat,
                            isSelected = category == cat,
                            onClick = { category = cat }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    serviceCategories.drop(3).forEach { cat ->
                        ServiceCategoryChip(
                            label = cat,
                            isSelected = category == cat,
                            onClick = { category = cat }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Submit button
            Button(
                onClick = {
                    titleError       = title.isBlank()
                    providerError    = provider.isBlank()
                    descriptionError = description.isBlank()
                    locationError    = location.isBlank()
                    phoneError       = phone.isBlank()
                    ratingError      = rating.isBlank()

                    val allValid = !titleError && !providerError &&
                            !descriptionError && !locationError &&
                            !phoneError && !ratingError

                    if (allValid) {
                        val newService = Service(
                            title = title,
                            provider = provider,
                            description = description,
                            location = location,
                            phone = phone,
                            rating = rating,
                            category = category
                        )
                        viewModel.addService(newService)
                        showSuccessDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealColor),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "List Service 🔧",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddServiceScreenPreview() {
    AddServiceScreen(rememberNavController())
}