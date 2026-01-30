package com.gradle.aicodeapp.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gradle.aicodeapp.network.model.Todo
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.viewmodel.TodoViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoFormPage(
    todoId: Int? = null,
    viewModel: TodoViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    var selectedDate by rememberSaveable { mutableStateOf(viewModel.getTodayDate()) }
    var selectedType by rememberSaveable { mutableStateOf(Todo.TYPE_WORK) }
    var selectedPriority by rememberSaveable { mutableStateOf(Todo.PRIORITY_NORMAL) }
    
    var showDatePicker by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate
    )
    
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    
    val isEditMode = todoId != null
    val pageTitle = if (isEditMode) "编辑待办" else "新建待办"
    
    LaunchedEffect(todoId) {
        if (isEditMode && todoId != null) {
            val todo = uiState.todos.find { it.id == todoId }
            if (todo != null) {
                title = todo.title
                content = todo.content
                selectedDate = todo.date
                selectedType = todo.type
                selectedPriority = todo.priority
            }
        }
    }
    
    val isFormValid = title.isNotBlank() && content.isNotBlank()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pageTitle) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(Spacing.Large),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("标题 *") },
                placeholder = { Text("请输入待办标题") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = title.isBlank() && isSubmitting,
                supportingText = if (title.isBlank() && isSubmitting) {
                    { Text("标题不能为空") }
                } else null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                )
            )
            
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("内容 *") },
                placeholder = { Text("请输入待办内容") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                isError = content.isBlank() && isSubmitting,
                supportingText = if (content.isBlank() && isSubmitting) {
                    { Text("内容不能为空") }
                } else null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                )
            )
            
            OutlinedTextField(
                value = dateFormat.format(Date(selectedDate)),
                onValueChange = {},
                label = { Text("日期") },
                placeholder = { Text("选择日期") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "选择日期"
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                )
            )
            
            TypeSelector(
                selectedType = selectedType,
                onTypeSelected = { selectedType = it }
            )
            
            PrioritySelector(
                selectedPriority = selectedPriority,
                onPrioritySelected = { selectedPriority = it }
            )
            
            Spacer(modifier = Modifier.height(Spacing.Large))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
            ) {
                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("取消")
                }
                
                Button(
                    onClick = {
                        isSubmitting = true
                        if (isEditMode && todoId != null) {
                            viewModel.updateTodo(
                                id = todoId,
                                title = title,
                                content = content,
                                date = selectedDate,
                                type = selectedType,
                                priority = selectedPriority,
                                status = Todo.STATUS_INCOMPLETE
                            )
                        } else {
                            viewModel.addTodo(
                                title = title,
                                content = content,
                                date = selectedDate,
                                type = selectedType,
                                priority = selectedPriority
                            )
                        }
                        onBackClick()
                    },
                    enabled = isFormValid && !isSubmitting,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(if (isEditMode) "保存" else "创建")
                    }
                }
            }
        }
    }
    
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            selectedDate = it
                            showDatePicker = false
                        }
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("取消")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}

@Composable
fun TypeSelector(
    selectedType: Int,
    onTypeSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "类型",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(Spacing.Small))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            TypeChip(
                label = "工作",
                isSelected = selectedType == Todo.TYPE_WORK,
                onClick = { onTypeSelected(Todo.TYPE_WORK) },
                modifier = Modifier.weight(1f)
            )
            TypeChip(
                label = "生活",
                isSelected = selectedType == Todo.TYPE_LIFE,
                onClick = { onTypeSelected(Todo.TYPE_LIFE) },
                modifier = Modifier.weight(1f)
            )
            TypeChip(
                label = "娱乐",
                isSelected = selectedType == Todo.TYPE_ENTERTAINMENT,
                onClick = { onTypeSelected(Todo.TYPE_ENTERTAINMENT) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun TypeChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    ) {
        Text(label)
    }
}

@Composable
fun PrioritySelector(
    selectedPriority: Int,
    onPrioritySelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "优先级",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(Spacing.Small))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            PriorityChip(
                label = "高",
                isSelected = selectedPriority == Todo.PRIORITY_HIGH,
                onClick = { onPrioritySelected(Todo.PRIORITY_HIGH) },
                modifier = Modifier.weight(1f)
            )
            PriorityChip(
                label = "普通",
                isSelected = selectedPriority == Todo.PRIORITY_NORMAL,
                onClick = { onPrioritySelected(Todo.PRIORITY_NORMAL) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PriorityChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    ) {
        Text(label)
    }
}
