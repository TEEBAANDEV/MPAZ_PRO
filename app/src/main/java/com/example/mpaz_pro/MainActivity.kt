package com.example.mpaz_pro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.mpaz_pro.ui.navigation.AppNavHost
import com.example.mpaz_pro.ui.theme.MPAZ_PROTheme
import com.example.mpaz_pro.ui.viewmodel.AuthViewModel
import com.example.mpaz_pro.ui.viewmodel.StudentViewModel
import com.example.mpaz_pro.ui.viewmodel.TeacherViewModel
import com.example.mpaz_pro.ui.viewmodel.UtpViewModel

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels {
        val app = application as MpazApplication
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(app.repository) as T
            }
        }
    }

    private val utpViewModel: UtpViewModel by viewModels {
        val app = application as MpazApplication
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UtpViewModel(app.repository) as T
            }
        }
    }

    private val teacherViewModel: TeacherViewModel by viewModels {
        val app = application as MpazApplication
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TeacherViewModel(app.repository) as T
            }
        }
    }

    private val studentViewModel: StudentViewModel by viewModels {
        val app = application as MpazApplication
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return StudentViewModel(app.repository, app.ttsHelper, app.hapticHelper) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MPAZ_PROTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavHost(
                        navController = navController,
                        authViewModel = authViewModel,
                        utpViewModel = utpViewModel,
                        teacherViewModel = teacherViewModel,
                        studentViewModel = studentViewModel
                    )
                }
            }
        }
    }
}