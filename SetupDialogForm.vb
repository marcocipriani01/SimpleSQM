<ComVisible(False)>
Public Class SimpleSQM

    Private Sub OK_Button_Click(sender As Object, e As EventArgs) Handles OK_Button.Click
        ObservingConditions.comPort = ComboBoxComPort.SelectedItem
        Me.DialogResult = DialogResult.OK
        Me.Close()
    End Sub

    Private Sub Cancel_Button_Click(sender As Object, ByVal e As EventArgs) Handles Cancel_Button.Click
        Me.DialogResult = DialogResult.Cancel
        Me.Close()
    End Sub

    Private Sub ShowAscomWebPage(sender As Object, e As EventArgs) Handles PictureBox1.DoubleClick, PictureBox1.Click
        Try
            Process.Start("https://ascom-standards.org/")
        Catch noBrowser As ComponentModel.Win32Exception
            If noBrowser.ErrorCode = -2147467259 Then
                MessageBox.Show(noBrowser.Message)
            End If
        Catch other As Exception
            MessageBox.Show(other.Message)
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
