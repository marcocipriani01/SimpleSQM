Option Infer On
Option Strict On
Imports MetroFramework

<ComVisible(False)>
Public Class SimpleSQM

    Private Sub OK_Button_Click(sender As Object, e As EventArgs) Handles OK_Button.Click
        ObservingConditions.comPort = ComboBoxComPort.SelectedItem.ToString()
        DialogResult = DialogResult.OK
        Close()
    End Sub

    Private Sub Cancel_Button_Click(sender As Object, ByVal e As EventArgs) Handles Cancel_Button.Click
        Me.DialogResult = DialogResult.Cancel
        Me.Close()
    End Sub

    Private Sub ShowAscomWebPage(sender As Object, e As EventArgs) Handles PictureBox1.DoubleClick, PictureBox1.Click
        Try
            Process.Start("https://ascom-standards.org/")
        Catch ex As Exception
            MetroMessageBox.Show(Me, "Error! " + ex.Message, "SimpleSQM", MessageBoxButtons.OK, MessageBoxIcon.Error, 80)
        End Try
    End Sub

    Private Sub linkLabel_Click(sender As Object, e As EventArgs) Handles linkLabel.Click
        Try
            Process.Start("https://marcocipriani01.github.io/")
        Catch ex As Exception
            MetroMessageBox.Show(Me, "Error! " + ex.Message, "SimpleSQM", MessageBoxButtons.OK, MessageBoxIcon.Error, 80)
        End Try
    End Sub

    Private Sub SetupDialogForm_Load(sender As Object, e As EventArgs) Handles MyBase.Load
        InitUI()
    End Sub

    Private Sub InitUI()
        ComboBoxComPort.Items.Clear()
        ComboBoxComPort.Items.AddRange(IO.Ports.SerialPort.GetPortNames())
        If ComboBoxComPort.Items.Contains(ObservingConditions.comPort) Then
            ComboBoxComPort.SelectedItem = ObservingConditions.comPort
        End If
    End Sub
End Class