# CustomStackView

Android implementation of a custom stack view with 3 stacks.


  Custom stack view that will hold up to 3 stacks,
  In a header and a body Content view, the Header has ClickListener with hidden and show on click.
  The Custom view has three states,
  - EXPANDED - All three stacks have their body view shown,
  - COLLAPSED - All the three stacks have their body view hidden,
  - EXPAND_FIRST - The first view is Expanded, The rest two views are hidden.
 
  Each Child/Stack has a state either Expanded or collapsed, specified by a Boolean.
 
   Assumptions:
   - There will be always three stacks.
   - One stack or all three combined could be higher than the viewport, So wrapped in a ScrollView.
   - Layouts can be added via XML or programmatically.
   - No more than 3 stacks can be added Since in case of uncertainty of the number of stacks, something like a collapsible recycle view can be used.