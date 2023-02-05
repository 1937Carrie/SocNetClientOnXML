package sdumchykov.androidApp.presentation.contacts.dialogFragment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AddContactDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage("Додати контакт?")
            .setPositiveButton("OK") { _, _ -> }
            .setNegativeButton("Dismiss") { _, _ -> }
            .create()

    companion object {
        const val TAG = "AddContactDialog"
    }
}