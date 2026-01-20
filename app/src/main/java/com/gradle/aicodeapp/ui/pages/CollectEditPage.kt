package com.gradle.aicodeapp.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.gradle.aicodeapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectEditPage(
    articleId: Int,
    initialTitle: String,
    initialAuthor: String,
    initialLink: String,
    onBackClick: () -> Unit,
    onSubmit: (Int, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf(TextFieldValue(initialTitle)) }
    var author by remember { mutableStateOf(TextFieldValue(initialAuthor)) }
    var link by remember { mutableStateOf(TextFieldValue(initialLink)) }
    var titleError by remember { mutableStateOf<String?>(null) }
    var authorError by remember { mutableStateOf<String?>(null) }
    var linkError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(initialTitle, initialAuthor, initialLink) {
        title = TextFieldValue(initialTitle)
        author = TextFieldValue(initialAuthor)
        link = TextFieldValue(initialLink)
    }

    fun validateAndSubmit() {
        titleError = if (title.text.isBlank()) "标题不能为空" else null
        authorError = if (author.text.isBlank()) "作者不能为空" else null
        linkError = if (link.text.isBlank()) "链接不能为空" else null

        if (titleError == null && authorError == null && linkError == null) {
            onSubmit(articleId, title.text, author.text, link.text)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("编辑收藏") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF0F0F0),
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { 
                    title = it
                    titleError = null
                },
                label = { Text("文章标题") },
                modifier = Modifier.fillMaxWidth(),
                isError = titleError != null,
                supportingText = titleError?.let { { Text(it) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = author,
                onValueChange = { 
                    author = it
                    authorError = null
                },
                label = { Text("作者") },
                modifier = Modifier.fillMaxWidth(),
                isError = authorError != null,
                supportingText = authorError?.let { { Text(it) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = link,
                onValueChange = { 
                    link = it
                    linkError = null
                },
                label = { Text("文章链接") },
                modifier = Modifier.fillMaxWidth(),
                isError = linkError != null,
                supportingText = linkError?.let { { Text(it) } }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = ::validateAndSubmit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("保存修改")
            }
        }
    }
}
