<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()>
Partial Class SimpleSQM
    Inherits MetroFramework.Forms.MetroForm

    'Form overrides dispose to clean up the component list.
    <System.Diagnostics.DebuggerNonUserCode()>
    Protected Overrides Sub Dispose(ByVal disposing As Boolean)
        Try
            If disposing AndAlso components IsNot Nothing Then
                components.Dispose()
            End If
        Finally
            MyBase.Dispose(disposing)
        End Try
    End Sub

    'Required by the Windows Form Designer
    Private components As System.ComponentModel.IContainer

    'NOTE: The following procedure is required by the Windows Form Designer
    'It can be modified using the Windows Form Designer.  
    'Do not modify it using the code editor.
    <System.Diagnostics.DebuggerStepThrough()>
    Private Sub InitializeComponent()
        Dim resources As System.ComponentModel.ComponentResourceManager = New System.ComponentModel.ComponentResourceManager(GetType(SimpleSQM))
        Me.TableLayoutPanel1 = New System.Windows.Forms.TableLayoutPanel()
        Me.OK_Button = New MetroFramework.Controls.MetroButton()
        Me.Cancel_Button = New MetroFramework.Controls.MetroButton()
        Me.PictureBox1 = New System.Windows.Forms.PictureBox()
        Me.label2 = New MetroFramework.Controls.MetroLabel()
        Me.ComboBoxComPort = New System.Windows.Forms.ComboBox()
        Me.linkLabel = New MetroFramework.Controls.MetroLink()
        Me.TableLayoutPanel1.SuspendLayout()
        CType(Me.PictureBox1, System.ComponentModel.ISupportInitialize).BeginInit()
        Me.SuspendLayout()
        '
        'TableLayoutPanel1
        '
        resources.ApplyResources(Me.TableLayoutPanel1, "TableLayoutPanel1")
        Me.TableLayoutPanel1.Controls.Add(Me.OK_Button, 0, 0)
        Me.TableLayoutPanel1.Controls.Add(Me.Cancel_Button, 1, 0)
        Me.TableLayoutPanel1.Name = "TableLayoutPanel1"
        '
        'OK_Button
        '
        resources.ApplyResources(Me.OK_Button, "OK_Button")
        Me.OK_Button.FontSize = MetroFramework.MetroButtonSize.Medium
        Me.OK_Button.Name = "OK_Button"
        Me.OK_Button.Style = MetroFramework.MetroColorStyle.Red
        Me.OK_Button.Theme = MetroFramework.MetroThemeStyle.Dark
        Me.OK_Button.UseSelectable = True
        '
        'Cancel_Button
        '
        resources.ApplyResources(Me.Cancel_Button, "Cancel_Button")
        Me.Cancel_Button.DialogResult = System.Windows.Forms.DialogResult.Cancel
        Me.Cancel_Button.FontSize = MetroFramework.MetroButtonSize.Medium
        Me.Cancel_Button.Name = "Cancel_Button"
        Me.Cancel_Button.Style = MetroFramework.MetroColorStyle.Red
        Me.Cancel_Button.Theme = MetroFramework.MetroThemeStyle.Dark
        Me.Cancel_Button.UseSelectable = True
        '
        'PictureBox1
        '
        Me.PictureBox1.Cursor = System.Windows.Forms.Cursors.Hand
        Me.PictureBox1.Image = Global.ASCOM.SimpleSQM.My.Resources.Resources.ASCOM
        resources.ApplyResources(Me.PictureBox1, "PictureBox1")
        Me.PictureBox1.Name = "PictureBox1"
        Me.PictureBox1.TabStop = False
        '
        'label2
        '
        resources.ApplyResources(Me.label2, "label2")
        Me.label2.Name = "label2"
        Me.label2.Style = MetroFramework.MetroColorStyle.Red
        Me.label2.Theme = MetroFramework.MetroThemeStyle.Dark
        '
        'ComboBoxComPort
        '
        resources.ApplyResources(Me.ComboBoxComPort, "ComboBoxComPort")
        Me.ComboBoxComPort.FormattingEnabled = True
        Me.ComboBoxComPort.Name = "ComboBoxComPort"
        '
        'linkLabel
        '
        resources.ApplyResources(Me.linkLabel, "linkLabel")
        Me.linkLabel.Cursor = System.Windows.Forms.Cursors.Hand
        Me.linkLabel.Name = "linkLabel"
        Me.linkLabel.Theme = MetroFramework.MetroThemeStyle.Dark
        Me.linkLabel.UseSelectable = True
        '
        'SimpleSQM
        '
        Me.AcceptButton = Me.OK_Button
        resources.ApplyResources(Me, "$this")
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.CancelButton = Me.Cancel_Button
        Me.Controls.Add(Me.linkLabel)
        Me.Controls.Add(Me.ComboBoxComPort)
        Me.Controls.Add(Me.label2)
        Me.Controls.Add(Me.PictureBox1)
        Me.Controls.Add(Me.TableLayoutPanel1)
        Me.MaximizeBox = False
        Me.Name = "SimpleSQM"
        Me.Resizable = False
        Me.Style = MetroFramework.MetroColorStyle.Red
        Me.Theme = MetroFramework.MetroThemeStyle.Dark
        Me.TableLayoutPanel1.ResumeLayout(False)
        CType(Me.PictureBox1, System.ComponentModel.ISupportInitialize).EndInit()
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub
    Friend WithEvents TableLayoutPanel1 As System.Windows.Forms.TableLayoutPanel
    Friend WithEvents PictureBox1 As System.Windows.Forms.PictureBox
    Friend WithEvents ComboBoxComPort As System.Windows.Forms.ComboBox
    Friend WithEvents OK_Button As MetroFramework.Controls.MetroButton
    Friend WithEvents Cancel_Button As MetroFramework.Controls.MetroButton
    Private WithEvents label2 As MetroFramework.Controls.MetroLabel
    Friend WithEvents linkLabel As MetroFramework.Controls.MetroLink
End Class
